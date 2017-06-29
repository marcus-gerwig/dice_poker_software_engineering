package de.htwg.se.dicepoker.model.tableComponent

import de.htwg.se.dicepoker.util.AppConst
import scala.compat.Platform.EOL
import de.htwg.se.dicepoker.model.tableComponent.DiceCup

//noinspection ScalaStyle
case class Player(name: String, diceCount: Int = AppConst.number_of_dice, diceCup: DiceCup) {

  def this(name: String) = this(name, AppConst.number_of_dice, DiceCup(Nil))
  def this() = this("unknown", AppConst.number_of_dice, DiceCup(Nil))
  def hasLostRound: Player = copy(name, diceCount - 1)
  def hasLostGame: Boolean = if (diceCount == 0) true else false
  override def toString = "Name: " + name + "\t |Number_Of_Dice: " + diceCount + "\t |DiceCup: " + diceCup.toString() + EOL
  def equals(player: Player) = if (name.equals(player.name)) true else false
}