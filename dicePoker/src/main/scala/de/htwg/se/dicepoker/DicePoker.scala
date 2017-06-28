package de.htwg.se.dicepoker

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.aview.Tui
import de.htwg.se.dicepoker.aview.Gui

object DicePoker {

  val controller = new DPController(null)
  val gui = new Gui(controller)
 // val tui = new Tui(controller)

  def main(args: Array[String]): Unit = {

  // tui.processInputLine()

  }
}
