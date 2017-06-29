package de.htwg.se.dicepoker.model.tableComponent

import de.htwg.se.dicepoker.model.ITable

//noinspection ScalaStyle
case class PokerTable(players: Vector[Player]) extends ITable{

  def updateTable(players: Vector[Player]) = copy(players)

  def rollTheDice = {
    var newPlayers: Vector[Player] = Vector()
    for (p <- players) {
      val newDiceCup = p.diceCup.roll(p.diceCount).sortDiceCup
      val newPlayer = p.copy(p.name, p.diceCount, newDiceCup)
      newPlayers = newPlayers :+ newPlayer
    }
    copy(newPlayers)
  }

  override def toString = {
    var string = ""
    for (p <- players) {
      string += p.toString()
    }
    string
  }
}