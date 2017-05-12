package de.htwg.se.dicepoker.model

case class Round(highestBid: Bid) {
  def this() = this(null)
  def setHighestBid(newBid: Bid) = copy(newBid)
  def theRoundWins: Player = {
    var playerHasLost:Player = new Player()
    if(highestBid.doesPlayerLie) playerHasLost = null
    playerHasLost = highestBid.bidPlayer
    playerHasLost
  }
}