package de.htwg.se.dicepoker.model
import scala.util.Random

case class DiceCup(dieCombi: List[Int] = Nil) {

  def roll(dice: Int): DiceCup = copy((1 to dice).toList.map(x => scala.util.Random.nextInt(6) + 1))
  def countTuples = dieCombi.groupBy(l => l).map(t=>(t._1,t._2.length))
  def getMaxResult ={
     val tuples = this.countTuples
     var maxFrequency = 0
     var respDieValue = 1
     
     respDieValue = tuples.keys.max
     
     
     maxFrequency=tuples.values.max 
     
     if (maxFrequency == 1) respDieValue
     
     else (
     
     
     )
         
     
     for ((key, value)<-tuples){
       
       
     }
         
       
    
    
  }
}