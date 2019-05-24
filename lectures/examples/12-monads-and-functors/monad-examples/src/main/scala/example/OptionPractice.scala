package example

import MonadInstances._

trait OptionPractice {

  def sqrt(x: Double): Option[Double]

  def validateLeadingCoefficient(lc: Double): Option[Double]

  def getRoots(a: Double, b: Double, c: Double): Option[(Double, Double)]

  def addSquareRoots(x1: Double, x2: Double): Option[Double]

  def getSumOfSquares(a: Double, b: Double, c: Double): Option[Double]

  def printSumOfSquareRoots(a: Double, b: Double, c: Double): Unit =
    getSumOfSquares(a, b, c) match {
      case Some(sum) => println(s"Result: $sum")
      case None => println("No solution")
    }
}
