package de.htwg.se.dicepoker

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.aview.Tui
import scala.io.StdIn.readLine

object DicePoker {

  val controller = new DPController(null)
  val tui = new Tui(controller)

  def main(args: Array[String]): Unit = {
    var continue: Boolean = true
    do {
      continue = tui.processInputLine()
    } while (continue)

  }
}
