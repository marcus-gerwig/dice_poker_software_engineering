package de.htwg.se.dicepoker.util

trait Observer {
  def update(e: Event): Unit
}

abstract class Event
object DiceWereRollen extends Event
object PlayerHasWon extends Event
object PlayerWithHighestBidLied extends Event
object PlayerWithHighestBidNotLied extends Event

class Observable {
  var subscribers: Vector[Observer] = Vector()
  def add(s: Observer): Unit = subscribers = subscribers :+ s
  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)
  def notifyObservers(e: Event): Unit = subscribers.foreach(o => o.update(e))
}