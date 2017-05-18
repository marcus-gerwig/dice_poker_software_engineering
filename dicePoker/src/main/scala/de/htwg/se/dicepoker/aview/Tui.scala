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

  var lastLoser: Player = new Player(null)
  controller.add(this)
  controller.startGame(initPlayer(AppConst.number_of_player))
  textExplainCommands

  def initPlayer(number: Int) = {
    var players: Vector[Player] = Vector.empty
    println("Welcome to Dice Poker!")
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

    do {
      textInsertBid(playerStarts)
      input = readLine
    } while (!controller.bidIsValid(input, playerStarts))
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
    println("-- Highest bid at the moment = " + controller.getHighestBidResult(round))
    println("-- Now it's your turn " + playerFollows.name)
    println("-- Do you mistrust " + playerStarted.name + " or do you want to set a higher bid?")
    println("-- mistrust: 'm' | setHigherBid: 'b'")
    readLine
  }

  def raiseBid(playerRaises: Player, round: Round): Round = {
    println("->->->->->->->->->->->->")
    var input = ""
    var bid: Bid = null
    var newRound: Round = new Round()
    do {
      println(playerRaises.name + ", please declare a higher bid than " + round.highestBid.bidResult + " :")
      input = readLine
      if (controller.bidIsValid(input, playerRaises)) {
        bid = controller.newBid(input, playerRaises)
        newRound = controller.raiseHighestBid(bid, round)
      }
    } while (newRound == null)
    newRound
  }


  def readLine = scala.io.StdIn.readLine()
  def textInsertBid(player: Player) = println(player.name + ", please declare your bid (e.g. 3,2 /means your bid is a double of 3):")
  def textExplainCommands: Unit = println("start game: 's' | exit game: 'q' | restart: 'r'")
  def textExitMessage = println("The game is over. See you soon!");
  def textPlayerWinsRound(winner: Player) = {
    println(winner.name + " has won this round!")
    println("_________________________________________")
  }
  def textWinnerMessage(winner: Player) = println("...and the winner is " + winner.name)

  override def update(e: Event): Unit = {
    e match {
      case DiceWereRollen => println(EOL + controller.table.toString())
      case PlayerHasWon => println("Congratulations!")
      case PlayerWithHighestBidLied => println(lastLoser.name + " lied. His actual result was " + controller.playerResult(lastLoser))
      case PlayerWithHighestBidNotLied => {
        val winner = controller.whichPlayerFollows(lastLoser)
        println(winner.name + " did not lie. His actual result was " + controller.playerResult(winner))
      }
    }

  }
}