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

  var playerLostLastRound: Player = new Player(null)
  controller.add(this)
  val players = initPlayer(AppConst.number_of_player)
  controller.startGame(players)
  explainCommands

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
        exitMessage
        continue = false
      }
      case "s" => {
        while (!controller.gameIsOver) newRound
        val winner = controller.whoWonTheGame
        winnerMessage(winner)
        exitMessage
        continue = false
      }
      case "r" =>
    }

    continue
  }

  def explainCommands: Unit = println("start game: 's' | exit game: 'q' | restart: 'r'")
  def exitMessage = println("The game is over. See you soon!")
  def winnerMessage(winner: Player) = println("...and the winner is " + winner.name)

  def newRound: Unit = {
    controller.rolling
    val playerStarts: Player = if (playerLostLastRound.name == null) controller.whichPlayerStarts else playerLostLastRound
    val playerFollows: Player = controller.whichPlayerFollows(playerStarts)
    var input = ""

    do {
      println(playerStarts.name + ", please declare your bid (e.g. 3,2 /means your bid is a double of 3):")
      input = readLine
    } while (!controller.bidIsValid(input, playerStarts))
    val bid = controller.newBid(input, playerStarts)
    var round = controller.newRound(bid)
    continue(round, playerStarts, playerFollows)

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

  def askPlayerIfTrusts(round: Round, playerStarted: Player, playerFollows: Player): String = {
    println("-- Highest bid at the moment = " + controller.getHighestBid(round).bidResult)
    println("-- Now it's your turn " + playerFollows.name)
    println("-- Do you mistrust " + playerStarted.name + " or do you want to set a higher bid?")
    println("-- mistrust: 'm' | setHigherBid: 'b'")
    readLine
  }

  def continue(round: Round, playerStarted: Player, playerFollowed: Player): Unit = {
    val resp = askPlayerIfTrusts(round, playerStarted, playerFollowed)
    var roundCopy = round
    var roundWinner: Player = null
    resp match {
      case "b" => {
        roundCopy = raiseBid(playerFollowed, round)
        continue(roundCopy, playerFollowed, playerStarted)

      }
      case "m" => {
        roundWinner = controller.solveRound(round)
        playerLostLastRound = if (playerStarted.equals(roundWinner)) playerFollowed else playerStarted
        if (playerLostLastRound.equals(round.highestBid.bidPlayer)) controller.playerLied
        else
          println(roundWinner.name + " has won this round!")
        println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
      }
    }

  }

  override def update(e: Event): Unit = {
    e match {
      case DiceWereRollen => println(EOL + controller.table.toString())
      case PlayerHasWon => println("Congratulations!")
      case PlayerWithHighestBidLied => println(playerLostLastRound.name + " lied. His actual result was " + controller.playerResult(playerLostLastRound))
      case PlayerWithHighestBidNotLied => {
        val winner = controller.whichPlayerFollows(playerLostLastRound)
        println(winner.name + " did not lie. His actual result was " + controller.playerResult(winner))
      }
    }

  }

  def readLine = scala.io.StdIn.readLine()

}