package actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object Counter {
  trait CounterProtocol
  case class IncreaseCountWith(value: Int) extends CounterProtocol
  case object GetCount extends CounterProtocol
  case class Count(value: Int, numberOfIncreases: Int) extends CounterProtocol

}

class Counter extends Actor {
  import Counter._

  def receive: Receive = countBehaviour(0, 0)

  def countBehaviour(currentCount: Int, numberOfIncreases: Int): Receive = {
    case IncreaseCountWith(n) =>
      context.become(countBehaviour(currentCount + n, numberOfIncreases + 1))
    case GetCount =>
      sender ! Count(currentCount, numberOfIncreases)
  }
}

class Worker(counter: ActorRef) extends Actor {
  import Counter._

  def receive: Receive = {
    case "Start" =>
      println("Starting work...")

      counter ! IncreaseCountWith(1)
      counter ! IncreaseCountWith(2)
      counter ! IncreaseCountWith(3)
      counter ! GetCount
      counter ! IncreaseCountWith(4)
      counter ! IncreaseCountWith(5)
      counter ! IncreaseCountWith(6)
      counter ! GetCount

      println("Work finished")

    case Count(n, times) =>
      println(s"Current counter is $n, has been increased $times times")
  }
}

object ActorsExample2 extends App {
  val actorSystem = ActorSystem()

  val counter = actorSystem.actorOf(Props[Counter])
  val worker = actorSystem.actorOf(Props(new Worker(counter)))

  worker ! "Start"
}