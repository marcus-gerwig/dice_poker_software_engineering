package de.htwg.se.dicepoker.model
import de.htwg.se.dicepoker.model.tableComponent.{Player, PokerTable}

trait ITable {
  def rollTheDice:ITable
  def players:Vector[Player]
  def updateTable(players: Vector[Player]): ITable
}