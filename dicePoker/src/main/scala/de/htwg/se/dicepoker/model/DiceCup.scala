

package de.htwg.se.dicepoker.model
import scala.util.Random

case class DiceCup(dieCombi: List[Int]) {

  def this() = this(Nil)

  def roll(numDice: Int): DiceCup = copy((1 to numDice).toList.map(x => scala.util.Random.nextInt(6) + 1))

  def countTuples(): Map[Int, Int] = dieCombi.groupBy(l => l).map(t => (t._1, t._2.length))
  def cupHasTuples(tuples: Map[Int, Int]): Boolean = if (tuples.values.max > 1) true else false
  def sortDiceCup = copy(dieCombi.sorted)

  override def toString = {
    var string = ""
    for (d <- dieCombi) {
      string += d + " "
    }
    string
  }

  def getMaxResult(): Result = {
    val tuples = countTuples()
    var maxFrequency: Int = 0
    val highDie = tuples.keys.max
    if (cupHasTuples(tuples)) {
      var key = tuples.head._1
      var value = tuples.head._2

      for ((x, y) <- tuples) {
        if ((y >= value && x > key) || (y > value)) {
          value = y
          key = x
        }
      }
      Result(key, value)
    } else Result(highDie, 1)

  }

  //Approximately not used, initially developed for test purpose
  def countTuples(list: List[Int]): Map[Int, Int] = list.groupBy(l => l).map(t => (t._1, t._2.length))
  def getMaxResult(tuples: Map[Int, Int]): Result = {
    var maxFrequency: Int = 0
    val highDie = tuples.keys.max
    if (cupHasTuples(tuples)) {
      var key = tuples.head._1
      var value = tuples.head._2

      for ((x, y) <- tuples) {
        if ((y >= value && x > key) || (y > value)) {
          value = y
          key = x
        }
      }
      Result(key, value)
    } else Result(highDie, 1)

  }

}