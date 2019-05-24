package example

trait BasicExample {

  def sqrt(x: Double): Double = librarySqrt(x)

  def getRoots(a: Double, b: Double, c: Double): (Double, Double) = {
    val D = discriminant(a, b, c)
    val d = sqrt(D)
    val x1 = (-b + d) / (2 * a)
    val x2 = (-b - d) / (2 * a)
    (x1, x2)
  }

  def addSquareRoots(x1: Double, x2: Double): Double = sqrt(x1) + sqrt(x2)

  def getSumOfSquareRoots(a: Double, b: Double, c: Double): Double = {
    val roots = getRoots(a, b, c)
    addSquareRoots(roots._1, roots._2)
  }

  def printSumOfSquarRoots(a: Double, b: Double, c: Double): Unit =
    println(s"Result: ${getSumOfSquareRoots(a, b, c)}")
}
