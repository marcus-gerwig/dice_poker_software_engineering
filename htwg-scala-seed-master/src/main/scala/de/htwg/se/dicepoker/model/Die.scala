package de.htwg.se.dicepoker.model

//class Die {
//  var dieValue: Int = ThrowADie.throwDie
//  
//}

case class Die(var dieValue: Int) {
  
  def sameValueAs(secDie: Die):Boolean = this.dieValue == secDie.dieValue
}