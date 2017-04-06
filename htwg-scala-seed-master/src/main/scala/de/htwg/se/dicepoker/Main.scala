package de.htwg.se.dicepoker

import de.htwg.se.dicepoker.model.Student
import de.htwg.se.dicepoker.model.DiceCup
import de.htwg.se.dicepoker.model.Die

object Hello {
  def main(args: Array[String]): Unit = {
    val student = Student("Anderl")
    val fstDie: Die = new Die
    val secDie = new Die
    val dieArray = Array(fstDie, secDie)
    val cup = new DiceCup(5, dieArray)
    println("Hello, " + student.name)
    println("Das ist ein Test")

    //Dies ist ein Kommentar zum Testen des Branching
    dieArray.foreach { wuerfel: Die => println("This is die with hashCode " + wuerfel.hashCode()) }
    
  }
  //Dies ist ein Test für Git
}
