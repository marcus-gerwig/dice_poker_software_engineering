package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.{PokerTable, Player}

class DPController(var table: PokerTable) {
  def createPokerTable(players: List[Player]): Unit = {
    table = new PokerTable(players)
  }
  
}