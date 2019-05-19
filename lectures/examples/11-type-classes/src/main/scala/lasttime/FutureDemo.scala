package lasttime

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._

object FutureDemo extends App {
  def calc[T](expr: => T) = Future {
    Thread.sleep(4000)

    expr
  }

  val futureA = calc(42)
  val futureB = calc(10)

  val sum = for {
    a <- futureA
    b <- futureB
  } yield a + b

  println {
    Await.result(sum, 5.seconds)
  }
}
