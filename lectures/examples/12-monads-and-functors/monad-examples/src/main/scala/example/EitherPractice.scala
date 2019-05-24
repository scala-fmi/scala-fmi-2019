package example

import MonadInstances._

trait EitherPractice {
  type EitherMonad[+A] = Either[String, A]

  def sqrt(x: Double): Either[String, Double]

  def validateLeadingCoefficient(lc: Double): Either[String, Double]

  def getRoots(a: Double, b: Double, c: Double): Either[String, (Double, Double)]

  def addSquareRoots(x1: Double, x2: Double): Either[String, Double]

  def getSumOfSquareRoots(a: Double, b: Double, c: Double): Either[String, Double]

  def printSumOfSquareRoots(a: Double, b: Double, c: Double): Unit =
    getSumOfSquareRoots(a, b, c) match {
      case Right(sum) => println(s"Result: $sum")
      case Left(errorMessage) => println(errorMessage)
    }
}
