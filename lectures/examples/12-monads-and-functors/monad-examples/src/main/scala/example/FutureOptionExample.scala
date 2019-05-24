package example

import example.MonadInstances._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

trait FutureOptionExample {
  type OptionTMonad[+A] = OptionT[Future, A]

  def sqrt(x: Double): Option[Double] = if (x >= 0) Some(librarySqrt(x)) else None

  def deepSqrt(x: Double): Future[Option[Double]] = Future {
    val time = 2000L
    Thread.sleep(time)
    println("Thinking...")
    sqrt(x)
  }

  def validateLeadingCoefficient(lc: Double): Option[Double] =
    if (lc != 0) Some(lc) else None

  def getRoots(a: Double, b: Double, c: Double): OptionT[Future, (Double, Double)] = {
    val D = discriminant(a, b, c)
    for {
      d <- OptionT(deepSqrt(D))
      lc <- OptionT(Monad[Future].unit(validateLeadingCoefficient(a)))
    } yield rootHelper(lc, b, d)
  }

  def addSquareRoots(x1: Double, x2: Double): OptionT[Future, Double] =
    Monad[OptionTMonad].map2(OptionT(deepSqrt(x1)), OptionT(deepSqrt(x2)))(_ + _)

  def getSumOfSquareRoots(a: Double, b: Double, c: Double): OptionT[Future, Double] =
    for {
      roots <- getRoots(a, b, c)
      sum <- addSquareRoots(roots._1, roots._2)
    } yield sum

  def printSumOfSquareRoots(a: Double, b: Double, c: Double): Unit =
    getSumOfSquareRoots(a, b, c).run.onComplete {
      case Success(Some(sum)) => println(s"Result: $sum")
      case Success(None) => println("No solution")
      case Failure(e) => println(s"Something happened: ${e.getMessage}")
    }
}


