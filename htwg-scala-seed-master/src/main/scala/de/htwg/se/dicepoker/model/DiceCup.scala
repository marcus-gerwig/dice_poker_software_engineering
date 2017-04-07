package de.htwg.se.dicepoker.model


case class DiceCup(var highestDieCombination: List[Die]=Nil) {
 
  
  def roll:List[Int] = {
    val actualDice = List(new Die, new Die, new Die, new Die, new Die)
    for (die:Die <- actualDice)  yield die.roll 
  }
}