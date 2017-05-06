package de.htwg.se.dicepoker.model

class TUI {

  def processInputLine(input: String) {
    println("Press 's' to start a game with 2 Players")
    input match {
      case "s" => {
        start2PGame()
      }

      case _ => {
        println("Wrong input: please try again!")
        processInputLine(input)
      }
    }

  }

  def start2PGame() {
    var cupP1 = new DiceCup
    cupP1.roll(5)
    var cupP2 = new DiceCup
    cupP2.roll(5)
    println("Game Started: The Dice are cast!")
    println("Player 1 rolled: " + cupP1.dieCombi)
    var map = Map(1 -> 2, 3 -> 1, 4 -> 1, 5 -> 1)
    println("The best result for Player 1 is " + cupP1.getMaxResult(map))
       
  }
  def changePlayer(){
    
    println("Change Player: if Player at the Keyboard has changed  press 'c' ")
    val scanner = new java.util.Scanner(System.in)
    val line = scanner.nextLine()
    line match {
      case "c" => {
        start2PGame()
      }

      case _ => {
        println("Wrong input: please try again!")
        start2PGame();
      }

    }

    
  }
  
}