package tasks

import http.HttpClient
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.asynchttpclient.Response

import scala.concurrent.Future

object TaskExample extends App {
  def randomResult(upTo: Int): Future[Response] =
    HttpClient.getScalaFuture(s"https://www.random.org/integers/?num=1&min=1&max=$upTo&col=1&base=10&format=plain")

  val taskA = Task {
    println("taskA")
    2 + 3 + 5
  }
  val taskB = Task.evalOnce {
    println("taskB")
    7 + 11 + 13
  }

  val taskC = Task.zipMap2(taskA, taskB)(_ + _)

  val result = taskC.flatMap { c =>
    Task.deferFuture(randomResult(c))
  }

  result.foreach(println)
  result.foreach(println)

  Thread.sleep(2000)
}
