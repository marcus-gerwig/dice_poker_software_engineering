package de.htwg.se.dicepoker.model

object Dice {
  1+3                                             //> res0: Int(4) = 4
  scala.util.Random.nextInt(6)+1                  //> res1: Int = 6
  var dice =6                                     //> dice  : Int = 6
  (1 to dice).toList.map(x=>scala.util.Random.nextInt(6)+1)
                                                  //> res2: List[Int] = List(5, 4, 3, 2, 3, 3)
}