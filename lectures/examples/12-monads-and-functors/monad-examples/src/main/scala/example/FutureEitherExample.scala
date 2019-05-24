package example

import example.MonadInstances._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

trait FutureEitherExample {
  type EitherTMonad[+A] = EitherT[Future, String, A]

  def sqrt(x: Double): Either[String, Double] =
    if (x >= 0) Right(librarySqrt(x)) else Left(squareRootOfNegativeNumberError(x))

  def deepSqrt(x: Double): Future[Either[String, Double]] = Future {
    val time = 2000L
    Thread.sleep(time)
    println("Thinking...")
    sqrt(x)
  }

  def validateLeadingCoefficient(lc: Double): Either[String, Double] =
    if (lc != 0) Right(lc) else Left(leadingCoefficientError)

  def getRoots(a: Double, b: Double, c: Double): EitherT[Future, String, (Double, Double)] = {
    val D = discriminant(a, b, c)
    for {
      d <- EitherT(deepSqrt(D).map(_.left.map(_ => negativeDiscriminantError(D))))
      lc <- EitherT(Monad[Future].unit(validateLeadingCoefficient(a)))
    } yield rootHelper(lc, b, d)
  }

  def addSquareRoots(x1: Double, x2: Double): EitherT[Future, String, Double] =
    Monad[EitherTMonad].map2(
      EitherT(deepSqrt(x1)).mapEither(_.left.map(_ => negativeRootError(x1))),
      EitherT(deepSqrt(x2)).mapEither(_.left.map(_ => negativeRootError(x2)))
    )(_ + _)

  def getSumOfSquareRoots(a: Double, b: Double, c: Double): EitherT[Future, String, Double] =
    for {
      roots <- getRoots(a, b, c)
      sum <- addSquareRoots(roots._1, roots._2)
    } yield sum

  def printSumOfSquareRoots(a: Double, b: Double, c: Double): Unit =
    getSumOfSquareRoots(a, b, c).run.onComplete {
      case Success(Right(sum)) => println(s"Result: $sum")
      case Success(Left(error)) => println(error)
      case Failure(exception) => println(exception.getMessage)
    }
}
