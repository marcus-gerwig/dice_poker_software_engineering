package de.htwg.se.dicepoker.model

case class PokerTable(players: Vector[Player]) {
  def addPlayer(newPlayer: Player) = copy(players :+ newPlayer)
  def numberOfPlayers = players.size
  
  def rollTheDice = {
    var newPlayers = Vector.empty
    for (p <- players) {
      val newDiceCup = p.diceCup.roll(p.diceCount)
      val newPlayer = p.copy(p.name, p.playerResult, p.diceCount, newDiceCup)
      newPlayers :+ newPlayer
    }
    copy(newPlayers)
  }
  
  override def toString = ""+ players.map { p => p.toString() }
}