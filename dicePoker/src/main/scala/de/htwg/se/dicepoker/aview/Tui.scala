package de.htwg.se.dicepoker.aview

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.model.Player
import de.htwg.se.dicepoker.model.PokerTable
import de.htwg.se.dicepoker.model.DiceCup
import de.htwg.se.dicepoker.model.Round
import de.htwg.se.dicepoker.util.Observer
import de.htwg.se.dicepoker.util.AppConst

class Tui(controller: DPController) extends Observer {

  controller.add(this)
  controller.startGame(initPlayer(AppConst.number_of_player))
  explainCommands
  
  def initPlayer(number: Int) = {
    var players: Vector[Player] = Vector.empty
    println("Welcome to Dice Poker!")
    for (i <- 1 to number) {
      println("Hello Player " + i)
      println("Please enter your name:")
      val name = scala.io.StdIn.readLine()
      println("")
      players =  players :+ controller.newPlayer(name)
    }
    println("Alright, let the show begin...")
    players
  }

  def processInputLine(): Boolean = {
    println("INPUT:")
    var continue = true;
    val input = scala.io.StdIn.readLine()
    input match {
      case "q" => {
        exitMessage
        continue = false
      }
      case "s" =>{
        
      }
      case "r" => 
    }
    
    continue
  }
  
  
 
  def explainCommands:Unit = println("start game: 's' | exit game: 'q' | restart: 'r'")
  def exitMessage = println("The game is over. See you soon!")
  
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

    val round = new Round()

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

  override def update: Unit = PokerTable.toString()
}