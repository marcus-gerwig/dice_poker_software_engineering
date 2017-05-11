package de.htwg.se.dicepoker.model

case class Result(dieValue: Int, frequency: Int) {
  def saveResult(value: Int, freq: Int): Result = copy(value, freq)

  override def toString: String = {
    var result = ""
    for (i <- 0 until frequency) {
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