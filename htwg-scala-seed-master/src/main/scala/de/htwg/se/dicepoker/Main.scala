package de.htwg.se.dicepoker

import de.htwg.se.dicepoker.model.Student
import de.htwg.se.dicepoker.model.DiceCup
import de.htwg.se.dicepoker.model.Die
import de.htwg.se.dicepoker.model.ThrowADie

object Hello {
  def main(args: Array[String]): Unit = {
    val student = Student("Anderl")
    val fstDie = Die(ThrowADie.throwDie)
    val secDie = Die(ThrowADie.throwDie)
    println(fstDie sameValueAs secDie)
    println(fstDie.dieValue + " " + secDie.dieValue)

    val cup = new DiceCup(Array(fstDie, secDie))
    cup.shakeCup
    println(fstDie.dieValue + " " + secDie.dieValue)
  }
}
