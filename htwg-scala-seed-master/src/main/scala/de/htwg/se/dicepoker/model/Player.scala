package de.htwg.se.dicepoker.model

case class Player (name: String, playerResult: Result, diceCount: Int) {
  def this(name:String, diceCount:Int) = this(name, Result(1,1), diceCount)
  def hasLostRound: Player = copy(name, playerResult, diceCount-1)
  def hasLostGame: Boolean = if (diceCount == 0) true else false
}