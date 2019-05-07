package concurrent

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

object TaskExample extends App {
  Http.client

  val taskA = Task { 2 + 3 + 5 }
  val taskB = Task { 7 + 11 + 13 }

  val taskC = Task.zipMap2(taskA, taskB)(_ + _)

  val result = taskC.flatMap { c =>
    Task.deferFuture(Http.getScalaFuture(s"https://www.google.com/search?q=$c"))
  }

  result.runAsync.foreach(println)
  result.runAsync.foreach(println)
}
