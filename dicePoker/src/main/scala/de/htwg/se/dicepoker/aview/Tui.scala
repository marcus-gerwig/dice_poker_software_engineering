package de.htwg.se.dicepoker.aview

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.model._
import de.htwg.se.dicepoker.util._
import org.apache.log4j._

import scala.compat.Platform.EOL


//noinspection ScalaStyle
class Tui(controller: DPController) extends Observer {



  private[this] val log = Logger.getLogger(getClass().getName());
 // log.setLevel(Level.INFO);

  val tui = AppConst.tui_symbol_inFrontOfText
  //  listenTo(controller)
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
      case WelcomeMsg => log.info("Welcome to €€ DICE POKER €€!")
      case EnterPlayerName => {
        val index: Int = EnterPlayerName.attachment.asInstanceOf[Int]
        log.info("Hello Player " + index + EOL + "Please enter your name:")
        val name = readLine
        controller.setPlayerName(index, name)
       // log.info()
      }
      case LetShowBegin => log.info("Alright, let the show begin...")
      case ExplainCommands => log.info(tui + "start game: 's' | exit game: 'q' | restart: 'r'")
      case DiceWereRollen => log.info(EOL + controller.printTable)
      case PlayerHasWonRound => {
        val winner = controller.playerName(PlayerHasWonRound.attachment.asInstanceOf[Player])
        log.info(tui + winner + " has won this round!" + EOL + "_________________________________________")
      }
      case PlayerWithHighestBidLied => log.info(tui + controller.playerName(controller.getHighestBidPlayer) + " lied. His actual result was " + controller.playerResult(controller.getHighestBidPlayer) + ".")
      case PlayerWithHighestBidNotLied => {
        val winner = controller.whichPlayerFollows(controller.getLastLoser.get)
        log.info(tui + controller.playerName(winner.get) + " did not lie. His actual result was " + controller.playerResult(winner.get) + ".")
      }
      case NewRound => log.info(tui + "New Round")
      case DeclareFirstBid => {
        log.info(tui + controller.playerName(controller.getPlayerStarted.get) + ", please declare the first bid (e.g. 3,2 /means your bid is a double of 3):")
        val input = readLine
        controller.declareFirstBid(input)
      }
      case LineSeparator => log.info("->->->->->->->->->->->->")
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
        log.info()
        log.info(tui + "Highest bid at the moment = " + controller.getHighestBidResult)
        log.info(tui + "Now it's your turn " + controller.playerName(playerFoll.get))
        log.info(tui + "Do you mistrust " + controller.playerName(playerHigh) + " or do you want to set a higher bid?")
        log.info(tui + "mistrust: 'm' | setHigherBid: 'b'")
        val input = readLine
        input match {
          case "m" => controller.playerMistrusts
          case "b" => controller.playerRaisesBid(playerFoll.get)
        }
      }
      case PrintPlayer => {
        val player: Player = PrintPlayer.attachment.asInstanceOf[Player]
        log.info(controller.printPlayer(player))
      }
      case RequestHigherBid => {
        val player: Player = RequestHigherBid.attachment.asInstanceOf[Player]
        log.info(tui + controller.playerName(player) + ", please declare a higher bid than " + controller.getHighestBidResult + " :")
        val input = readLine
        controller.declareHigherBid(input, player)
      }
      case GameIsOver => {
        val winner = controller.playerName(GameIsOver.attachment.asInstanceOf[Player])
        log.info(tui + "...and the winner is " + winner + "!")
        log.info(tui + "Congratulations, " + winner + "!")
      }
      case GameWasCancelled => log.info(tui + "The game is over. See you soon!")
    }
  }
}