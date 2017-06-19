package de.htwg.se.dicepoker.model

import org.scalatest.WordSpec
import org.scalatest.Matchers
import de.htwg.se.dicepoker.model.impl.Player
import de.htwg.se.dicepoker.model.impl.Result
import de.htwg.se.dicepoker.model.impl.DiceCup
import de.htwg.se.dicepoker.model.impl.Bid

class BidSpec extends WordSpec with Matchers{
  
  "A Bid" can {
    val aDiceCup = DiceCup(List(1,2,3,4,5))
    val aPlayer = Player("Testplayer", 5, aDiceCup) 
    var aBid = Bid(Result (1,1), aPlayer)
    "be a valid bid" in {
      aBid.inputIsValidBid("1,1", aPlayer) should be (true)
      aBid.inputIsValidBid("6,5", aPlayer) should be (true)
      aBid.inputIsValidBid("6,6", aPlayer) should be (false)
      aBid.inputIsValidBid("0,3", aPlayer) should be (false)
      aBid.inputIsValidBid("2,0", aPlayer) should be (false)
    }
    
    "be correct converted" in{
       aBid.convertStringToBid("1,1") should be (aBid.copy(new Result (1,1), aBid.bidPlayer))    
  }
    
  "be lied by a Player" in{
    aBid.doesPlayerLie should be (false)
    aBid = Bid(Result(1,3), aPlayer) 
    aBid.doesPlayerLie should be (true)
  }
  
}
}