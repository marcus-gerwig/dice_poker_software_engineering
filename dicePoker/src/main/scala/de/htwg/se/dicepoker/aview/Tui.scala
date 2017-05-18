package de.htwg.se.dicepoker.aview

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.model.{ _ }

import de.htwg.se.dicepoker.util.Observer
import de.htwg.se.dicepoker.util.AppConst

import scala.compat.Platform.EOL
import de.htwg.se.dicepoker.util.Event
import de.htwg.se.dicepoker.util.DiceWereRollen
import de.htwg.se.dicepoker.util.PlayerHasWon

class Tui(controller: DPController) extends Observer {

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
    val playerStarts: Player = controller.whichPlayerStarts
    val playerFollows: Player = controller.whichPlayerFollows(playerStarts)
    var input = ""

    do {
      println(playerStarts.name + ", please declare your bid (e.g. 3,2 /means your bid is a double of 3):")
      input = readLine
    } while (!controller.bidIsValid(input))
    val bid = controller.newBid(input, playerStarts)
    var round = controller.newRound(bid)
    println("-- Highest bid at the moment = " + controller.getHighestBid(round).bidResult)
    println("-- Now it's your turn " + playerFollows.name)
    println("-- Do you mistrust " + playerStarts.name + " or do you want to set a higher bid?")
    println("-- mistrust: 'm' | setHigherBid: 'b'")
    val resp = readLine
    var roundWinner: Player = null
    resp match {
      case "m" => roundWinner = controller.solveRound(round)
      case "b" => {

      }
    }

    println(roundWinner.name + " has won this round!")
    println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
  }

  override def update(e: Event): Unit = {
    e match {
      case DiceWereRollen => println(EOL + controller.table.toString())
      case PlayerHasWon => println("Congratulations!")
    }

  }

  def readLine = scala.io.StdIn.readLine()

  def startGame2P {
    println("Press 's' to start game with 2 Players")
    println("Enter name of Player 1:")
    val player1 = new Player("Marcus", null, 5, null)
    println("Enter name of Player 2:")
    val player2 = new Player("Andy", null, 5, null)

    val playerlist: Vector[Player] = Vector(player1, player2)

    val table = new PokerTable(playerlist)

    println("A 2 Player game was started!")

    var diceCupP1 = new DiceCup

    diceCupP1 = diceCupP1.roll(player1.diceCount)

    var diceCupP2 = new DiceCup

    diceCupP2 = diceCupP2.roll(player2.diceCount)

    val round = new Round(null)

    while (!player1.hasLostGame || !player2.hasLostGame) {

      println("Player 1 has thrown" + player1.diceCount + " Dice")
      println("Player 1 has thrown this values: " + diceCupP1.dieCombi)
      println("Player 1s highest result: " + diceCupP1.getMaxResult(diceCupP1.countTuples(diceCupP1.dieCombi)))

      println("Player 1 please insert your bid (e.g. 2,1): ")

      println("Change Player")
      println("The bid of Player 1 is: ")

      round.setHighestBid(null)

      println("Player 2: If you don't trust press 's' if you want to enter a higher Bid press 'b'")
      println("Player 2 has thrown" + player2.diceCount + " Dice")
      println("Player 2 has thrown this values: " + diceCupP2.dieCombi)
      println("Player 2s highest result: " + diceCupP2.getMaxResult(diceCupP2.countTuples(diceCupP2.dieCombi)))

      println("Player 2 please insert your bid (e.g. 2,1): ")

      println("Change Player")
      println("The bid of Player 2 is: ")

      round.setHighestBid(null)

      println("Player 1: If you don't trust press 's' if you want to enter a higher Bid press 'b'")

    }

    if (player1.hasLostGame) {

      println("Player 2 has won the game")

    }

    if (player2.hasLostGame) {

      println("Player 1 has won the game")

    }

  }

}