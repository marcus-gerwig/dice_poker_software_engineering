package de.htwg.se.dicepoker.aview

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.model.tableComponent._

import de.htwg.se.dicepoker.util.Observer
import de.htwg.se.dicepoker.util.AppConst

import scala.compat.Platform.EOL
import de.htwg.se.dicepoker.util.Event
import de.htwg.se.dicepoker.util.DiceWereRollen
import de.htwg.se.dicepoker.util.PlayerHasWonRound
import de.htwg.se.dicepoker.util.PlayerWithHighestBidLied
import de.htwg.se.dicepoker.util.PlayerWithHighestBidNotLied
import de.htwg.se.dicepoker.util.DeclareFirstBid
import de.htwg.se.dicepoker.util.Input
import de.htwg.se.dicepoker.util.AskIfMistrusts
import de.htwg.se.dicepoker.util.PrintPlayer
import de.htwg.se.dicepoker.util.RequestHigherBid
import de.htwg.se.dicepoker.util.NewRound
import de.htwg.se.dicepoker.util.GameIsOver
import de.htwg.se.dicepoker.util.ExplainCommands
import de.htwg.se.dicepoker.util.GameWasCancelled
import de.htwg.se.dicepoker.util.WelcomeMsg
import de.htwg.se.dicepoker.util.EnterPlayerName
import de.htwg.se.dicepoker.util.LetShowBegin
import de.htwg.se.dicepoker.util.LineSeparator
import de.htwg.se.dicepoker.model.tableComponent.Player

class Tui(controller: DPController) extends Observer {
  val tui = AppConst.tui_symbol_inFrontOfText
  controller.add(this)
  controller.createPlayers

  def processInputLine() = {
    controller.menuNavigation
  }

  def readLine = scala.io.StdIn.readLine()

  override def update(e: Event): Unit = {
    e match {
      case WelcomeMsg => println("Welcome to €€ DICE POKER €€!")
      case EnterPlayerName => {
        val index: Int = EnterPlayerName.attachment.asInstanceOf[Int]
        println("Hello Player " + index + EOL + "Please enter your name:")
        val name = readLine
        controller.setUserInteraction(name)
        println()
      }
      case LetShowBegin => println("Alright, let the show begin...")
      case ExplainCommands => println(tui + "start game: 's' | exit game: 'q' | restart: 'r'")
      case DiceWereRollen => println(EOL + controller.printTable)
      case PlayerHasWonRound => {
        val winner = controller.playerName(PlayerHasWonRound.attachment.asInstanceOf[Player])
        println(tui + winner + " has won this round!" + EOL + "_________________________________________")
      }
      case PlayerWithHighestBidLied => println(tui + controller.playerName(controller.getHighestBidPlayer) + " lied. His actual result was " + controller.playerResult(controller.getHighestBidPlayer) + ".")
      case PlayerWithHighestBidNotLied => {
        val winner = controller.whichPlayerFollows(controller.getLastLoser)
        println(tui + controller.playerName(winner) + " did not lie. His actual result was " + controller.playerResult(winner) + ".")
      }
      case NewRound => println(tui + "New Round")
      case DeclareFirstBid => println(tui + controller.playerName(controller.getPlayerStarted) + ", please declare the first bid (e.g. 3,2 /means your bid is a double of 3):")
      case LineSeparator => println("->->->->->->->->->->->->")
      case Input => {
        val input = readLine
        controller.setUserInteraction(input)
      }
      case AskIfMistrusts => {
        println()
        println(tui + "Highest bid at the moment = " + controller.getHighestBidResult)
        println(tui + "Now it's your turn " + controller.playerName(controller.whichPlayerFollows(controller.getHighestBidPlayer)))
        println(tui + "Do you mistrust " + controller.playerName(controller.getHighestBidPlayer) + " or do you want to set a higher bid?")
        println(tui + "mistrust: 'm' | setHigherBid: 'b'")
        val input = readLine
        controller.setUserInteraction(input)
      }
      case PrintPlayer => {
        val player: Player = PrintPlayer.attachment.asInstanceOf[Player]
        println(controller.printPlayer(player))
      }
      case RequestHigherBid => {
        val player: Player = RequestHigherBid.attachment.asInstanceOf[Player]
        println(tui + controller.playerName(player) + ", please declare a higher bid than " + controller.getHighestBidResult + " :")
      }
      case GameIsOver => {
        val winner = controller.playerName(GameIsOver.attachment.asInstanceOf[Player])
        println(tui + "...and the winner is " + winner + "!")
        println(tui + "Congratulations, " + winner + "!")
      }
      case GameWasCancelled => println(tui + "The game is over. See you soon!")
    }
  }
}