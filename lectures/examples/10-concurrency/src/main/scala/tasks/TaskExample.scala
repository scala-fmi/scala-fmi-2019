package tasks

import http.Http
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.asynchttpclient.Response

import scala.concurrent.Future

object TaskExample extends App {
  Http.client

  def randomResult(upTo: Int): Future[Response] =
    Http.getScalaFuture(s"https://www.random.org/integers/?num=1&min=1&max=$upTo&col=1&base=10&format=plain")

  val taskA = Task { 2 + 3 + 5 }
  val taskB = Task { 7 + 11 + 13 }

  val taskC = Task.zipMap2(taskA, taskB)(_ + _)

  val result = taskC.flatMap { c =>
    Task.deferFuture(randomResult(c))
  }

  result.runAsync.foreach(println)
  result.runAsync.foreach(println)
}
