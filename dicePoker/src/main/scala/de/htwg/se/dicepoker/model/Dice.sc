package de.htwg.se.dicepoker.model

import scala.util.Random;

object Dice {
val aRes = Result(3,1)                            //> aRes  : de.htwg.se.dicepoker.model.Result = 3
val aRes2 = aRes.saveResult(2, 4)                 //> aRes2  : de.htwg.se.dicepoker.model.Result = 2 2 2 2
aRes2.toString()                                  //> res0: String = 2 2 2 2
}