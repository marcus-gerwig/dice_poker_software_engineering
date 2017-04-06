package de.htwg.se.dicepoker.model
import java.util.Random

class Die {
  var dieValue: Int = throwDie
  
  def throwDie: Int = new Random().nextInt((6 - 1 + 1) + 1)
  
}