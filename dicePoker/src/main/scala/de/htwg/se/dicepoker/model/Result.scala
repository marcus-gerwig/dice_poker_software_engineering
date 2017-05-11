package de.htwg.se.dicepoker.model

case class Result(dieValue: Int, frequency: Int) {
  def saveResult(value: Int, freq: Int): Result = copy(value, freq)

  override def toString: String = {
    var result = "" + dieValue
    for (i <- 0 to frequency) {
      result += " " + dieValue
    }
    return result
  }
  
  def isHigherThan(otherResult: Result) = {
    if (this.dieValue > otherResult.dieValue) true
    else if(this.dieValue == otherResult.dieValue && this.frequency > otherResult.frequency) true
    else false
  }
  
}