package de.htwg.se.dicepoker.model

import de.htwg.se.dicepoker.util.AppConst
import scala.compat.Platform.EOL

case class Player(name: String, playerResult: Result, diceCount: Int = AppConst.number_of_dice, diceCup: DiceCup) {

  def this(name: String, diceCount: Int) = this(name, Result(1, 1), diceCount, DiceCup(Nil))
  def this(name: String) = this(name, Result(1, 1), AppConst.number_of_dice, DiceCup(Nil))
  def this() = this("unknown", Result(1,1), AppConst.number_of_dice, DiceCup(Nil))
  def hasLostRound: Player = copy(name, playerResult, diceCount - 1)
  def hasLostGame: Boolean = if (diceCount == 0) true else false
  def setBid(dieValue: Int, freq: Int): Bid = new Bid(Result(dieValue, freq), this)
 override def toString = "Name: " + name + "\t|Number_Of_Dice: "+diceCount+"\t|DiceCup: "+diceCup.toString()+EOL

}