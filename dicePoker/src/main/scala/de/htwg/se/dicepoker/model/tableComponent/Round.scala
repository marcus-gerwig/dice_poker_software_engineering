package de.htwg.se.dicepoker.model.tableComponent

import de.htwg.se.dicepoker.model.tableComponent.Player
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