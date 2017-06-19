package de.htwg.se.dicepoker.model.roundComponent

import de.htwg.se.dicepoker.model.playerComponent.Player
import de.htwg.se.dicepoker.model.tableComponent.Bid

case class Round(highestBid: Bid) {
  def this() = this(null)
  def setHighestBid(newBid: Bid) = copy(newBid)
  def theRoundWins(opponent: Player): Player = {
    var winner: Player = new Player()
    if (highestBid.doesPlayerLie) winner = opponent
    else winner = highestBid.bidPlayer
    winner
  }
}