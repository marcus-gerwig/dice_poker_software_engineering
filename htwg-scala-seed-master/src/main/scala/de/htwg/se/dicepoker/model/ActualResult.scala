package de.htwg.se.dicepoker.model

case class ActualResult(actualDieValue: Int, actualFrequency: Int) {
  def saveActualResult(value: Int, freq: Int): ActualResult = copy(value, freq)
  
  override def toString: String = {
    var result = ""
    for (i <- 0 until actualFrequency) {
      result += " " + actualDieValue
    }
    return result
  }
}