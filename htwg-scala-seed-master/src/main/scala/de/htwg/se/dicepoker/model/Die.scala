package de.htwg.se.dicepoker.model
import java.util.Random


case class Die() {
  def roll:Int = new Random().nextInt(6 - 1 + 1) + 1

}