package de.htwg.se.dicepoker.model

import scala.util.Random;

object Dice {
  1 + 3                                           //> res0: Int(4) = 4
  scala.util.Random.nextInt(6) + 1                //> res1: Int = 6
  var dice = 5                                    //> dice  : Int = 5
  (1 to dice).toList.map(x => scala.util.Random.nextInt(6) + 1)
                                                  //> res2: List[Int] = List(3, 2, 1, 4, 6)
 
case class Die(value: Int) {
  def roll: Die = copy(new Random().nextInt(6) + 1)
}
var cup = new DiceCup                             //> cup  : de.htwg.se.dicepoker.model.DiceCup = DiceCup(List())
var cup2 = cup.roll(5)                            //> cup2  : de.htwg.se.dicepoker.model.DiceCup = DiceCup(List(4, 5, 4, 4, 2))
cup2.dieCombi.groupBy(l => l).map(t=>(t._1,t._2.length))
                                                  //> res3: scala.collection.immutable.Map[Int,Int] = Map(2 -> 1, 5 -> 1, 4 -> 3)
                                                  //| 
}