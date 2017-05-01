package de.htwg.se.dicepoker.model

case class Round(highestBid: Bid) {
  def setHighestBid(newBid: Bid) = copy(newBid)
}