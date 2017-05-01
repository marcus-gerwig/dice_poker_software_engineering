package de.htwg.se.dicepoker.model

case class PokerTable (players: List[Player]) {
  def addPlayer(newPlayer : Player) = copy(newPlayer :: players)
  def numberOfPlayers = players.size
}