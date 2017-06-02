package de.htwg.se.dicepoker.aview

import scala.swing._
import scala.swing.event._
import de.htwg.se.dicepoker.controller.DPController

class Gui(controller: DPController) extends Frame {
  title = "Dice Poker"

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("New") { controller.initPlayer })
      contents += new MenuItem(Action("Quit") { System.exit(0) })
    }
  }

}