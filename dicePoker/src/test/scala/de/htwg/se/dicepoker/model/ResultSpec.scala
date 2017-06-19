package de.htwg.se.dicepoker.model

import org.scalatest.WordSpec
import org.scalatest.Matchers
import de.htwg.se.dicepoker.model.impl.Result

class ResultSpec extends WordSpec with Matchers{
  
  "A Result" can{
    
    val aResult = Result(3, 2)
    val otherResult = Result(4, 2)
    
    "be higher" in{
      
      aResult.isHigherThan(otherResult) should be (true)
      
    }
  }
  
}