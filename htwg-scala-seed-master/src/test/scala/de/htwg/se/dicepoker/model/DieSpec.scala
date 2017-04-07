package de.htwg.se.dicepoker.model

import org.scalatest._
import org.scalatest.Matchers._

class DieSpec extends WordSpec {
  "A Die" should {
    "have a value between 1 and 6" in {
      val die = Die(ThrowADie.throwDie)
      die.dieValue should (be >= 1 and be <= 6)
    }
  }

  "Two dice" can {
    "have the same value" in {
      val fstDie = Die(5)
      val secDie = Die(5)
      fstDie.sameValueAs(secDie) should be(true)
    }
    "have different values" in {
      val fstDie = Die(5)
      val secDie = Die(4)
      fstDie.sameValueAs(secDie) should be(false)
    }
  }

}