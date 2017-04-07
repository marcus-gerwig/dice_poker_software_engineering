package de.htwg.se.dicepoker.model
import java.util.Random

object ThrowADie {
  def throwDie = new Random().nextInt(6 - 1 + 1) + 1
}