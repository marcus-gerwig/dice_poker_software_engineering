package de.htwg.se.dicepoker

import de.htwg.se.dicepoker.controller.controllerComponent.DPController
import de.htwg.se.dicepoker.aview.Tui
import de.htwg.se.dicepoker.aview.Gui
import de.htwg.se.dicepoker.controller.IController

object DicePoker {

  val controller:IController = new DPController(null)
  //val gui = new Gui(controller)
 val tui = new Tui(controller)

  def main(args: Array[String]): Unit = {

  tui.processInputLine()

  }
}
