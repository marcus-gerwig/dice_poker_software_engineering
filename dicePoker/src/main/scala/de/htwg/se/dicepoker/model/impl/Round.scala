package de.htwg.se.dicepoker.model.impl

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