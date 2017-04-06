package de.htwg.se.dicepoker.model

//removed numberOfDice:Int
case class DiceCup(actualDice: Array[Die], highestDieCombination: Array[Die]) {
  def this(actualDice: Array[Die]) {
    this(actualDice, null)
  }

  def shakeCup = actualDice.foreach { die: Die => die.dieValue = ThrowADie.throwDie }
}