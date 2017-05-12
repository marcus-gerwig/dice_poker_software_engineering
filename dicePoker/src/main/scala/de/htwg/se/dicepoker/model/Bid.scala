package de.htwg.se.dicepoker.model
import scala.util.matching

case class Bid(bidResult: Result, bidPlayer: Player) {
  def this() = this(null, null)

  def inputIsValidBid(input: String): Boolean = {
    val date = raw"[1-6],[1-6]".r
    input match {
      case date(_*) => true
      case _ => false
    }
  }

  def convertStringToBid(input: String): Bid = copy(new Result(input.charAt(0).asDigit, input.charAt(2).asDigit), this.bidPlayer)
  def doesPlayerLie: Boolean = {
    val hisDiceCup = this.bidPlayer.diceCup
    val hisActualResult = hisDiceCup.getMaxResult()
    if (bidResult.isHigherThan(hisActualResult)) true
    else false
  }
}