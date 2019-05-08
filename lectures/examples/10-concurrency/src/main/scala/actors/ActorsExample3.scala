package actors

import actors.Person.Greet
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object Person {
  sealed trait PersonProtocol
  case class Greet(person: ActorRef)
  case object Hello
  case object WhatsYourName
  case class NameReply(name: String)

  def props(name: String) = Props(new Person(name))
}

class Person(name: String) extends Actor {
  import Person._

  def receive: Receive = start

  def start:  Receive = {
    case Greet(person) =>
      person ! Hello

      context.become(sentGreetings(person))
    case Hello =>
      println(s"$name was greeted")

      sender ! Hello

      context.become(greeted(sender))
  }

  def sentGreetings(to: ActorRef): Receive = {
    case Hello =>
      println(s"$name was greeted")

      sender ! WhatsYourName

    case NameReply(otherPersonName) =>
      if (sender == to)
        println(s"$name received other person name: $otherPersonName")
      else
        println(s"A stranger introduced themselves to me ($name)")

      context.become(start)
  }

  def greeted(by: ActorRef): Receive = {
    case WhatsYourName =>
      if (sender == by) {
        println(s"$name was asked for name")

        sender ! NameReply(name)
      } else println(s"$name asked for name by a stranger")

      context.become(start)
  }
}

object ActorsExample extends App {
  val actorSystem = ActorSystem()

  val george = actorSystem.actorOf(Person.props("George"))
  val john = actorSystem.actorOf(Person.props("John"))

  george ! Greet(john)
}
