package de.htwg.se.dicepoker.model

import de.htwg.se.dicepoker.util.AppConst
import scala.compat.Platform.EOL

case class Player(name: String, diceCount: Int = AppConst.number_of_dice, diceCup: DiceCup) {

  def this(name: String, diceCount: Int) = this(name, diceCount, DiceCup(Nil))
  def this(name: String) = this(name, AppConst.number_of_dice, DiceCup(Nil))
  def this() = this("unknown", AppConst.number_of_dice, DiceCup(Nil))
  def hasLostRound: Player = copy(name, diceCount - 1)
  def hasLostGame: Boolean = if (diceCount == 0) true else false
  def setBid(dieValue: Int, freq: Int): Bid = new Bid(Result(dieValue, freq), this)
 override def toString = "Name: " + name + "\t|Number_Of_Dice: "+diceCount+"\t|DiceCup: "+diceCup.toString()+EOL

}