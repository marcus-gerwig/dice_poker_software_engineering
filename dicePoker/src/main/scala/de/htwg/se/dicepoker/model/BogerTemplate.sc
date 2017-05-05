package de.htwg.se.dicepoker.model

import java.util.Observable

object BogerTemplate {


case class Person( name: String)
//
val p = new Person("Marko")
p.name

case class Course(var  students: List[Person])

val c = new Course(List(p))
c.students.head.name

class Registration (var course: Course) extends Observable {

  def register(p:Person): Unit = {
    course.students = course.students.::(p)
    notifyObserver
    
  }
}

val reg = new Registration (c)
reg.register(p)

reg.course.students.head


class Tui(reg:Registration) extends Observer{
  reg.add(this)
  override def update = display
  def display: Unit = {
    println(reg.course)
  }
}

val tui = new Tui(reg)

trait Observer {
  def update
}

class Observable {
  var observer:List[Observer]= Nil
  def add(o:Observer): Unit = {
    observer = observer.::(o)
  }
  def notifyObserver: Unit = {
    observer.foreach(o => o.update)
  }
}

reg.register(new Person("Tom"))
}