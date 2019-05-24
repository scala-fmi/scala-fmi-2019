package example

import MonadInstances._

trait EitherExample {
  type EitherMonad[+A] = Either[String, A]

  def sqrt(x: Double): Either[String, Double] =
    if (x >= 0) Right(librarySqrt(x)) else Left(squareRootOfNegativeNumberError(x))

  def validateLeadingCoefficient(lc: Double): Either[String, Double] =
    if (lc != 0) Right(lc) else Left(leadingCoefficientError)

  def getRoots(a: Double, b: Double, c: Double): Either[String, (Double, Double)] = {
    val D = discriminant(a, b, c)
    for {
      lc <- validateLeadingCoefficient(a)
      d <- sqrt(D).left.map(_ => negativeDiscriminantError(D))
    } yield rootHelper(lc, b, d)
  }

  def addSquareRoots(x1: Double, x2: Double): Either[String, Double] = {
    Monad[EitherMonad].map2(
      sqrt(x1).left.map(_ => negativeRootError(x1)),
      sqrt(x2).left.map(_ => negativeRootError(x2))
    )(_ + _)
  }

  def getSumOfSquareRoots(a: Double, b: Double, c: Double): Either[String, Double] =
    for {
      roots <- getRoots(a, b, c)
      sum <- addSquareRoots(roots._1, roots._2)
    } yield sum

  def printSumOfSquareRoots(a: Double, b: Double, c: Double): Unit =
    getSumOfSquareRoots(a, b, c) match {
      case Right(sum) => println(s"Result: $sum")
      case Left(errorMessage) => println(errorMessage)
    }
}
