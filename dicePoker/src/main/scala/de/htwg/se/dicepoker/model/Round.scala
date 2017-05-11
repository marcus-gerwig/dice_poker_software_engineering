package de.htwg.se.dicepoker.model

case class Round(highestBid: Bid) {
  def this() = this(null)
  def setHighestBid(newBid: Bid) = copy(newBid)
  
}