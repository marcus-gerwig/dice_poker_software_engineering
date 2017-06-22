package de.htwg.se.dicepoker.util
import de.htwg.se.dicepoker.model._
import scala.swing.event.Event

trait Observer {
  def update(e: Event): Unit
}


trait Attach {
  var attachment: Any = null
  def set(param: Any): Unit = attachment = param;
}

class WelcomeMsg extends Event
object EnterPlayerName extends Event with Attach
object LetShowBegin extends Event
object ExplainCommands extends Event
object DiceWereRollen extends Event
object PlayerHasWonRound extends Event with Attach
object PlayerWithHighestBidLied extends Event
object PlayerWithHighestBidNotLied extends Event
object NewRound extends Event
object DeclareFirstBid extends Event
object LineSeparator extends Event
object Input extends Event
object AskIfMistrusts extends Event
object PrintPlayer extends Event with Attach
object RequestHigherBid extends Event with Attach
object GameIsOver extends Event with Attach
object GameWasCancelled extends Event

class Observable {
  var subscribers: Vector[Observer] = Vector()
  def add(s: Observer): Unit = subscribers = subscribers :+ s
  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)
  def notifyObservers(e: Event): Unit = subscribers.foreach(o => o.update(e))
}