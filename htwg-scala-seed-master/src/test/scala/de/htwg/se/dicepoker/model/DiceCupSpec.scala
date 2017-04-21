package de.htwg.se.dicepoker.model

import org.scalatest.WordSpec
import org.scalatest.Matchers

class DiceCupSpec extends WordSpec with Matchers {
  "A DiceCup" can {
    val cup = DiceCup(Nil)
    val rolledCup = cup.roll(5)

    "contain dice with values between 1 and 6" in {
      rolledCup.dieCombi.foreach { x => x should (be >= 1 and be <= 6) }
    }

    "contain different numbers of dice" in {
      cup.roll(0).dieCombi.length should be(0)
      cup.roll(10).dieCombi.length should be(10)
    }

    "have value tuples" in {
      var list = List(1, 1, 5, 3, 4)
      rolledCup.countTuples(list) should be(Map(1 -> 2, 5 -> 1, 3 -> 1, 4 -> 1))
      list = List(6, 6, 6, 6, 6)
      rolledCup.countTuples(list) should be(Map(6 -> 5))
    }

    "not have value tuples" in {
      val list = List(1, 2, 3, 4, 5)
      val collection = rolledCup.countTuples(list)
      rolledCup.cupHasTuples(collection) should be(false)
    }

    "have a highest result due to the quantity and the strength of a die value" in {
      var map = Map(1 -> 2, 3 -> 1, 4 -> 1, 5 -> 1)
      rolledCup.getMaxResult(map) should be((1, 2))

      map = Map(1 -> 2, 3 -> 1, 5 -> 2)
      rolledCup.getMaxResult(map) should be((5, 2))

      map = Map(1 -> 3, 3 -> 1, 5 -> 2)
      rolledCup.getMaxResult(map) should be((1, 3))

      map = Map(1 -> 1, 3 -> 1, 4 -> 1, 5 -> 1, 6 -> 1)
      rolledCup.getMaxResult(map) should be((6, 1))
    }
  }

}