package de.htwg.se.dicepoker.model

import de.htwg.se.dicepoker.util.AppConst

case class Player(name: String, playerResult: Result, diceCount: Int = AppConst.NUMBER_OF_DICE, diceCup: DiceCup) {
  
  def this(name: String, diceCount: Int) = this(name, Result(1, 1), diceCount, DiceCup(Nil))
  def this(name: String) = this(name, Result(1, 1), AppConst.NUMBER_OF_DICE, DiceCup(Nil))
  def hasLostRound: Player = copy(name, playerResult, diceCount - 1)
  def hasLostGame: Boolean = if (diceCount == 0) true else false
  def setBid(dieValue: Int, freq: Int): Bid = new Bid(Result(dieValue, freq), this)
}