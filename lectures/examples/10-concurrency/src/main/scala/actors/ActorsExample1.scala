package actors

import akka.actor.{Actor, ActorSystem, Props}

class Human extends Actor {
  def receive: Receive = {
    case "Hello" =>
      println("Hello, how are you?")
  }
}

object ActorsExample1 extends App {
  val actorSystem = ActorSystem()

  val george = actorSystem.actorOf(Props[Human])

  george ! "Hello"
}
