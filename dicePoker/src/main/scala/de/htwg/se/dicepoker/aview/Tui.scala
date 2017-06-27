package de.htwg.se.dicepoker.aview

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.model._
import de.htwg.se.dicepoker.util._
import org.apache.log4j.{LogManager, Logger}

import scala.compat.Platform.EOL

//noinspection ScalaStyle
class Tui(controller: DPController) extends Observer {
  val tui = AppConst.tui_symbol_inFrontOfText
  val logger: Logger = Logger.getLogger(this.getClass.getName)
  controller.add(this)
  controller.createPlayers


  def processInputLine() = {
    controller.menuNavigation
  }

  def readLine = {
    val input = scala.io.StdIn.readLine()
    if (input == "Q") controller.stopGameWanted
    input
  }

  override def update(e: Event): Unit = {
    e match {
      case WelcomeMsg => logger.info("Welcome to €€ DICE POKER €€!")
      case EnterPlayerName => {
        val index: Int = EnterPlayerName.attachment.asInstanceOf[Int]
        logger.info("Hello Player " + index + EOL + "Please enter your name:")
        val name = readLine
        controller.setPlayerName(index, name)
        println()
      }
      case LetShowBegin => logger.info("Alright, let the show begin...")
      case ExplainCommands => logger.info(tui + "start game: 's' | exit game: 'q' | restart: 'r'")
      case DiceWereRollen => {
        logger.info(EOL + controller.printTable)
        controller.beginRound
      }
      case PlayerHasWonRound => {
        val winner = controller.playerName(PlayerHasWonRound.attachment.asInstanceOf[Player])
        logger.info(tui + winner + " has won this round!" + EOL + "_________________________________________")
        controller.newRound
      }
      case PlayerWithHighestBidLied => logger.info(tui + controller.playerName(controller.getHighestBidPlayer) + " lied. His actual result was " + controller.playerResult(controller.getHighestBidPlayer) + ".")
      case PlayerWithHighestBidNotLied => {
        val winner = controller.whichPlayerFollows(controller.getLastLoser.get)
        logger.info(tui + controller.playerName(winner.get) + " did not lie. His actual result was " + controller.playerResult(winner.get) + ".")
      }
      case NewRound => logger.info(tui + "New Round")
      case DeclareFirstBid => {
        logger.info(tui + controller.playerName(controller.getPlayerStarted.get) + ", please declare the first bid (e.g. 3,2 means your bid is a double of 3):")
        val input = readLine
        controller.declareFirstBid(input)
      }
      case LineSeparator => logger.info("->->->->->->->->->->->->")
      case Input => {
        val input = readLine
        input match {
          case "q" => controller.stopGameWanted
          case "r" => controller.menuNavigation
          case _ => controller.startGame
        }
      }
      case AskIfMistrusts => {
        val playerHigh = controller.getHighestBidPlayer
        val playerFoll = controller.whichPlayerFollows(controller.getHighestBidPlayer)
        logger.info()
        logger.info(tui + "Highest bid at the moment = " + controller.getHighestBidResult)
        logger.info(tui + "Now it's your turn " + controller.playerName(playerFoll.get))
        logger.info(tui + "Do you mistrust " + controller.playerName(playerHigh) + " or do you want to set a higher bid?")
        logger.info(tui + "mistrust: 'm' | setHigherBid: 'b'")
        val input = readLine
        input match {
          case "m" => controller.playerMistrusts
          case "b" => controller.playerRaisesBid(playerFoll.get)
        }
      }
      case PrintPlayer => {
        val player: Player = PrintPlayer.attachment.asInstanceOf[Player]
        logger.info(controller.printPlayer(player))
      }
      case RequestHigherBid => {
        val player: Player = RequestHigherBid.attachment.asInstanceOf[Player]
        logger.info(tui + controller.playerName(player) + ", please declare a higher bid than " + controller.getHighestBidResult + " :")
        val input = readLine
        controller.declareHigherBid(input, player)
      }
      case GameIsOver => {
        val winner = controller.playerName(GameIsOver.attachment.asInstanceOf[Player])
        logger.info(tui + "...and the winner is " + winner + "!")
        logger.info(tui + "Congratulations, " + winner + "!")
      }
      case GameWasCancelled => {
        logger.info(tui + "The game is over. See you soon!")

      }
    }
  }
}