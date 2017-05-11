package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.{ PokerTable, Player }
import de.htwg.se.dicepoker.util.Observable

class DPController(var table: PokerTable) extends Observable {

  def startGame(players: Vector[Player]):Unit = {
    createPokerTable(players)
    notifyObservers
  }

  def createPokerTable(players: Vector[Player]): Unit = {
    table = new PokerTable(players)
    notifyObservers
  }
  
   def gambling:Unit = {
    rollTheDice
    notifyObservers
   
  }

  def newPlayer(name: String): Player = new Player(name)
  def rollTheDice = table = table.rollTheDice
}