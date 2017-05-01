package de.htwg.se.dicepoker.model

import scala.util.Random;

object Dice {
val aRes = ActualResult(3,1)                      //> aRes  : de.htwg.se.dicepoker.model.ActualResult =  3
val aRes2 = aRes.saveActualResult(2, 4)           //> aRes2  : de.htwg.se.dicepoker.model.ActualResult =  2 2 2 2
aRes2.toString()                                  //> res0: String = " 2 2 2 2"
}