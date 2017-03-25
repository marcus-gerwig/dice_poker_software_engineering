package de.htwg.se.yourgame

import de.htwg.se.yourgame.model.Student

object Hello {
  def main(args: Array[String]): Unit = {
    val student = Student("Marcus")
    println("Hello, " + student.name)
    println("Das ist ein Test")
  }
  //Dies ist ein Test für Git
}
