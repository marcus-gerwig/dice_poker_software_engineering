package de.htwg.se.dicepoker.model
import scala.util.Random
case class DiceCup(var highestDieCombination: List[Die] = Nil) {

  def roll(numberOfDice: Int): List[Int] = (0 to numberOfDice).toList.map( x => Random.nextInt(6)+1)
}