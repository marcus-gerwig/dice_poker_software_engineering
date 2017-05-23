package de.htwg.se.dicepoker.model

import org.scalatest.WordSpec
import org.scalatest.Matchers

class PlayerSpec extends WordSpec with Matchers{
  "A Player" can{
    
    val aDiceCup = DiceCup(List(1,2,3,4,5))
    var aPlayer = new Player("Testplayer", 5, aDiceCup) 
    
    "lose Round" in{
      
      aPlayer.hasLostRound() should be (new Player("Testplayer", 4, aDiceCup));
    }
    "lose Game" in {
            
      aPlayer.hasLostGame() should be (false);
    }
    
    "is bidding" in {
      
      aPlayer.setBid(5, 2) should be (Bid(new Result (5,2),aPlayer))
    }
      
    }
  
  
  
}