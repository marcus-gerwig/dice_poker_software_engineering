package de.htwg.se.dicepoker.aview

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.model.Player
import de.htwg.se.dicepoker.model.PokerTable
import de.htwg.se.dicepoker.model.DiceCup
import de.htwg.se.dicepoker.model.Round

class Tui(controller: DPController) {
  
  
  def processInputLine(input: String): Unit = {
       
    
  }
    def startGame2P{
      println("Press 's' to start game with 2 Players")
      println("Enter name of Player 1:")
      val player1 = new Player("Marcus", null, 5, null)
      println("Enter name of Player 2:")
      val player2 = new Player("Andy", null, 5, null)
                                                           
      val playerlist: List[Player] = List(player1, player2)
      
      val table = new PokerTable (playerlist)
      
      println("A 2 Player game was started!")
      
      val diceCupP1 = new DiceCup(Nil)
      
      diceCupP1.roll(player1.diceCount)
      
      val diceCupP2 = new DiceCup(Nil)
      
      diceCupP2.roll(player2.diceCount)
      
      val round = new Round(null)  
      
      while(!player1.hasLostGame || !player2.hasLostGame){
          
        
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
      
     if(player1.hasLostGame){
       
       println("Player 2 has won the game")
       
     }
     
      if(player2.hasLostGame){
       
       println("Player 1 has won the game")
       
     }
      
      
      
      
      
    }

  
}