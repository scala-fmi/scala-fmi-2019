package example

import example.MonadInstances._

trait OptionExample {

  def sqrt(x: Double): Option[Double] =
    if (x >= 0) Some(librarySqrt(x)) else None

  def validateLeadingCoefficient(lc: Double): Option[Double] =
    if (lc != 0) Some(lc) else None

  def getRoots(a: Double, b: Double, c: Double): Option[(Double, Double)] = {
    val D = discriminant(a, b, c)
    for {
      d: Double <- sqrt(D)
      lc: Double <- validateLeadingCoefficient(a)
    } yield rootHelper(lc, b, d)
  }

  def addSquareRoots(x1: Double, x2: Double): Option[Double] =
    Monad[Option].map2(sqrt(x1), sqrt(x2))(_ + _)

  def getSumOfSquares(a: Double, b: Double, c: Double): Option[Double] =
    for {
      roots: (Double, Double) <- getRoots(a, b, c)
      sum: Double <- addSquareRoots(roots._1, roots._2)
    } yield sum

  def printSumOfSquareRoots(a: Double, b: Double, c: Double): Unit =
    getSumOfSquares(a, b, c) match {
      case Some(sum) => println(s"Result: $sum")
      case None => println("No solution")
    }
}
