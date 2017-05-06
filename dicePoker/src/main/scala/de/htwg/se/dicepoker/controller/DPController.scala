package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.{PokerTable, Player}

class DPController(var table: PokerTable) {
  def startGame() = {
    
  }
  
  def createPokerTable(players: List[Player]): Unit = {
    table = new PokerTable(players)
  }
  
  def addPlayerToTable(name:String) = table.addPlayer(new Player(name))
  
}