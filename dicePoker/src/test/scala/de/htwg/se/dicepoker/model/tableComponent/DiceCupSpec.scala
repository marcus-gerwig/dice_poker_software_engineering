package de.htwg.se.dicepoker.model.tableComponent

import org.scalatest.WordSpec
import org.scalatest.Matchers


class DiceCupSpec extends WordSpec with Matchers {
  "A DiceCup" can {
    val cup = DiceCup(Nil)
    var rolledCup = cup.roll(5)

    "contain dice with values between 1 and 6" in {
      rolledCup.dieCombi.foreach { x => x should (be >= 1 and be <= 6) }
    }

    "contain different numbers of dice" in {
      cup.roll(0).dieCombi.length should be(0)
      cup.roll(10).dieCombi.length should be(10)
    }

    "have value tuples" in {
      rolledCup = new DiceCup(List(1,1,5,3,4))

      rolledCup.countTuples() should be(Map(1 -> 2, 5 -> 1, 3 -> 1, 4 -> 1))
      rolledCup = new DiceCup(List(6, 6, 6, 6, 6))
      rolledCup.countTuples() should be(Map(6 -> 5))
    }

    "not have value tuples" in {
      rolledCup = new DiceCup(List(1, 2, 3, 4, 5))
      val collection = rolledCup.countTuples()
      rolledCup.cupHasTuples(collection) should be(false)
    }

    "have a highest result due to the quantity and the strength of a die value" in {
      var cup1 = new DiceCup(List(1,1,3,4,5))
      cup1.getMaxResult() should be(Result(1, 2))

      cup1 = new DiceCup(List(1,1,3,5,5))
      cup1.getMaxResult() should be(Result(5, 2))

      cup1 = new DiceCup(List(1,1,1,3,5,5))
      cup1.getMaxResult() should be(Result(1, 3))

      cup1 = new DiceCup(List(1,3,4,5,6))
      cup1.getMaxResult() should be(Result(6, 1))

    }
  }

}