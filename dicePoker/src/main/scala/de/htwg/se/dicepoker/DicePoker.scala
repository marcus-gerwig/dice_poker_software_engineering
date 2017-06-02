package de.htwg.se.dicepoker

import de.htwg.se.dicepoker.controller.DPController
import de.htwg.se.dicepoker.aview.Tui
import scala.io.StdIn.readLine
import de.htwg.se.dicepoker.aview.Gui

object DicePoker {

  val controller = new DPController(null)
  val tui = new Tui(controller)
  val gui = new Gui(controller)

  def main(args: Array[String]): Unit = {

    tui.processInputLine()

  }
}
