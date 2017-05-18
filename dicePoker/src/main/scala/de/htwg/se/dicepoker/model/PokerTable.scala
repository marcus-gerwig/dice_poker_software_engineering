package de.htwg.se.dicepoker.model

case class PokerTable(players: Vector[Player]) {
  def addPlayer(newPlayer: Player) = copy(players :+ newPlayer)
  def numberOfPlayers = players.length
  def updateTable(players: Vector[Player]) = copy(players)
  def getPlayerByName(name:String): Player = {
    for(p <- players){
      if(p.name.equals(name)) return p
    }
    return null
  }

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