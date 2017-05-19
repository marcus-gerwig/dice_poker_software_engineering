package de.htwg.se.dicepoker.aview

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.model.{ _ }

import de.htwg.se.dicepoker.util.Observer
import de.htwg.se.dicepoker.util.AppConst

import scala.compat.Platform.EOL
import de.htwg.se.dicepoker.util.Event
import de.htwg.se.dicepoker.util.DiceWereRollen
import de.htwg.se.dicepoker.util.PlayerHasWon
import de.htwg.se.dicepoker.util.PlayerWithHighestBidLied
import de.htwg.se.dicepoker.util.PlayerWithHighestBidNotLied

class Tui(controller: DPController) extends Observer {
  val tui = AppConst.tui_symbol_inFrontOfText
  var lastLoser: Player = null
  controller.add(this)
  controller.startGame(initPlayer(AppConst.number_of_player))
  textExplainCommands

  def initPlayer(number: Int) = {
    var players: Vector[Player] = Vector.empty
    println("Welcome to €€ DICE POKER €€!")
    for (i <- 1 to number) {
      println("Hello Player " + i)
      println("Please enter your name:")
      val name = readLine
      println("")
      players = players :+ controller.newPlayer(name)
    }
    println("Alright, let the show begin...")
    players
  }

  def processInputLine(): Boolean = {
    println("INPUT:")
    var continue = true;
    val input = readLine
    input match {
      case "q" => {
        textExitMessage
        continue = false
      }
      case "s" => {
        while (!controller.gameIsOver) newRound
        val winner = controller.whoWonTheGame
        textWinnerMessage(winner)
        textExitMessage
        continue = false
      }
      case "r" => continue = false
    }

    continue
  }

  def newRound: Unit = {
    controller.rolling
    val playerStarts: Player = controller.whichPlayerStarts(lastLoser)
    val playerFollows: Player = controller.whichPlayerFollows(playerStarts)
    var input = ""
    textNewRound
    do {
      textInsertBid(playerStarts)
      input = readLine
    } while (!controller.inputIsValid(input, playerStarts))
    val bid = controller.newBid(input, playerStarts)
    var round = controller.newRound(bid)
    continue(round, playerStarts, playerFollows)

  }

  def continue(round: Round, playerStarted: Player, playerFollowed: Player): Unit = {
    val resp = askPlayerIfMistrusts(round, playerStarted, playerFollowed)
    var roundCopy = round
    var roundWinner: Player = null
    resp match {
      case "b" => {
        roundCopy = raiseBid(playerFollowed, round)
        continue(roundCopy, playerFollowed, playerStarted)

      }
      case "m" => {
        roundWinner = controller.solveRound(round)
        lastLoser = if (playerStarted.equals(roundWinner)) playerFollowed else playerStarted
        if (lastLoser.equals(controller.getHighestBidPlayer(roundCopy))) controller.playerLied
        else controller.playerDidNotLie
        textPlayerWinsRound(roundWinner)
      }
    }

  }

  def askPlayerIfMistrusts(round: Round, playerStarted: Player, playerFollows: Player): String = {
    println()
    println(tui + "Highest bid at the moment = " + controller.getHighestBidResult(round))
    println(tui + "Now it's your turn " + playerFollows.name)
    println(tui + "Do you mistrust " + playerStarted.name + " or do you want to set a higher bid?")
    println(tui + "mistrust: 'm' | setHigherBid: 'b'")
    readLine
  }

  def raiseBid(playerRaises: Player, round: Round): Round = {
    println("->->->->->->->->->->->->")
    var input = ""
    var bid: Bid = null
    var newRound: Round = new Round()
    do {
      textPlayer(playerRaises)
      println(tui + playerRaises.name + ", please declare a higher bid than " + round.highestBid.bidResult + " :")
      input = readLine
      if (controller.inputIsValid(input, playerRaises) && controller.newBidIsHigher(input, round)) {
        bid = controller.newBid(input, playerRaises)
        newRound = controller.raiseHighestBid(bid, round)
      }
    } while (controller.getHighestBid(newRound) == null)
    newRound
  }

  def readLine = scala.io.StdIn.readLine()
  def textInsertBid(player: Player) = println(tui + player.name + ", please declare your bid (e.g. 3,2 /means your bid is a double of 3):")
  def textExplainCommands: Unit = println(tui + "start game: 's' | exit game: 'q' | restart: 'r'")
  def textExitMessage = println(tui + "The game is over. See you soon!");
  def textPlayerWinsRound(winner: Player) = {
    println(tui + winner.name + " has won this round!")
    println("_________________________________________")
  }
  def textWinnerMessage(winner: Player) = println(tui + "...and the winner is " + winner.name + "!")
  def textPlayer(player: Player) = println(controller.printPlayer(player))
  def textNewRound = println(tui + "New Round")

  override def update(e: Event): Unit = {
    e match {
      case DiceWereRollen => println(EOL + controller.printTable)
      case PlayerHasWon => println(tui + "Congratulations!")
      case PlayerWithHighestBidLied => println(tui + lastLoser.name + " lied. His actual result was " + controller.playerResult(lastLoser) + ".")
      case PlayerWithHighestBidNotLied => {
        val winner = controller.whichPlayerFollows(lastLoser)
        println(tui + winner.name + " did not lie. His actual result was " + controller.playerResult(winner) + ".")
      }
    }

  }
}