package de.htwg.se.dicepoker.model

import scala.util.Random;

object Dice {
var input ="2,4"                                  //> input  : String = 2,4
val bid = new Bid(null,null)                      //> bid  : de.htwg.se.dicepoker.model.Bid = Bid(null,null)
bid.inputIsValidBid(input, new Player("Test"))    //> res0: Boolean = true

}