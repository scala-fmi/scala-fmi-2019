package example

import example.MonadInstances._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

trait FutureEitherPractice {
  type EitherTMonad[+A] = EitherT[Future, String, A]

  def sqrt(x: Double): Either[String, Double] =
    if (x >= 0) Right(librarySqrt(x)) else Left("Square root of negative number")

  def deepSqrt(x: Double): Future[Either[String, Double]] = Future {
    val time = 2000L
    Thread.sleep(time)
    println("Thinking...")
    sqrt(x)
  }

  def validateLeadingCoefficient(lc: Double): Either[String, Double] =
    if (lc != 0) Right(lc) else Left("Leading coefficient equals zero")

  def getRoots(a: Double, b: Double, c: Double): EitherT[Future, String, (Double, Double)]

  def addSquareRoots(x1: Double, x2: Double): EitherT[Future, String, Double]

  def getSumOfSquareRoots(a: Double, b: Double, c: Double): EitherT[Future, String, Double]

  def printSumOfSquareRoots(a: Double, b: Double, c: Double): Unit =
    getSumOfSquareRoots(a, b, c).run.onComplete {
      case Success(Right(sum)) => println(s"Result: $sum")
      case Success(Left(error)) => println(error)
      case Failure(exception) => println(exception.getMessage)
    }
}
