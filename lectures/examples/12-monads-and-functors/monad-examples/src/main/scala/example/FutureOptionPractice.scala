package example

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

import MonadInstances._

trait FutureOptionPractice {
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

  def getRoots(a: Double, b: Double, c: Double): OptionT[Future, (Double, Double)]

  def addSquareRoots(x1: Double, x2: Double): OptionT[Future, Double]

  def getSumOfSquareRoots(a: Double, b: Double, c: Double): OptionT[Future, Double]

  def printSumOfSquares(a: Double, b: Double, c: Double): Unit =
    getSumOfSquareRoots(a, b, c).run.onComplete {
      case Success(Some(sum)) => println(s"Result: $sum")
      case Success(None) => println("No solution")
      case Failure(e) => println(s"Something happened: ${e.getMessage}")
    }
}


