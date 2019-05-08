package futures

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

import scala.concurrent.duration._

object FailSafeExample extends App {
  def double(n: Int, shouldFail: Boolean = false): Future[Int] = Future {
    println(s"Executing double with $n")

    if (shouldFail) throw new RuntimeException

    n * 2
  }

  val execution = for {
    a <- double(2)
    b <- double(a, true)
    c <- double(b)
  } yield c

  val result = Await.result(execution, 2.seconds)

  println(result)
}
