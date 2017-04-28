package de.htwg.se.dicepoker.model

import scala.util.Random;

object Dice {
  1 + 3                                           //> res0: Int(4) = 4
  scala.util.Random.nextInt(6) + 1                //> res1: Int = 1
  var dice = 5                                    //> dice  : Int = 5
  (1 to dice).toList.map(x => scala.util.Random.nextInt(6) + 1)
                                                  //> res2: List[Int] = List(2, 2, 3, 4, 5)

  case class Die(value: Int) {
    def roll: Die = copy(new Random().nextInt(6) + 1)
  }
  var cup = new DiceCup                           //> cup  : de.htwg.se.dicepoker.model.DiceCup = DiceCup(List())
  var cup2 = cup.roll(5)                          //> cup2  : de.htwg.se.dicepoker.model.DiceCup = DiceCup(List(1, 1, 4, 3, 5))

  val tuples = cup2.countTuples                   //> tuples  : scala.collection.immutable.Map[Int,Int] = Map(5 -> 1, 4 -> 1, 1 ->
                                                  //|  2, 3 -> 1)
  def hasTuples: Boolean = if (tuples.values.max > 1) true else false
                                                  //> hasTuples: => Boolean

  var maxFrequency: Int = 0                       //> maxFrequency  : Int = 0
  val highDie = tuples.keys.max                   //> highDie  : Int = 5

    if (hasTuples) {
      var key = tuples.head._1
      var value = tuples.head._2
      for ((x, y) <- tuples) {
        if (y >= value && x > key) {
          value = y
          key = x
        }
      }
      (key, value)
    } else (highDie, 1)                           //> res3: (Int, Int) = (5,1)
}