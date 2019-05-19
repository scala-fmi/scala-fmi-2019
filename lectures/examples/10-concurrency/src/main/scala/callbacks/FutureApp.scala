package callbacks

import scala.concurrent.Future

object FutureApp extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  def double(n: Int): Future[Int] = Future {
    n * 2
  }

  val calc1 = Future {
    println("calc1")
    Thread.sleep(2000)
    1 + 1
  }
  val calc2 = Future {
    println("calc2")
    Thread.sleep(2000)
    42 * 20
  }

  val result = for {
    (a, b) <- calc1.zip(calc2)
    c <- double(a + b)
  } yield c.toString

  result.foreach(println)

  Thread.sleep(4000)
}
