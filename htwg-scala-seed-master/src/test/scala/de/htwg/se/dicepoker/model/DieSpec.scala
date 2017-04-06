package de.htwg.se.dicepoker.model

import org.scalatest._
import org.scalatest.Matchers._

class DieSpec extends WordSpec {
  "A Die" should {
    "have a value" in {
      Die(5).dieValue should be(5)
    }
    
    "have a value between 1 and 6" in {
      val dieValue = Die(5).dieValue
      dieValue should (be >= 1 and be <= 6)
    }
  }

}