import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Await, ExecutionContext, Future}

package object example {
  val leadingCoefficientError = "Error: leading coefficient equals zero"

  def squareRootOfNegativeNumberError(x: Double) = s"Error: square root of negative number $x"

  def negativeDiscriminantError(D: Double) = s"Error: negative discriminant $D"

  def negativeRootError(root: Double) = s"Error: negative root $root"

  def discriminant(a: Double, b: Double, c: Double): Double = b * b - 4 * a * c

  def librarySqrt(x: Double): Double = math.pow(x, 0.5)

  def rootHelper(a: Double, b: Double, d: Double): (Double, Double) = {
    val x1 = (-b + d) / (2 * a)
    val x2 = (-b - d) / (2 * a)
    (x1, x2)
  }

  implicit class FutureOps[A](future: Future[A]) {
    def evaluate: A = Await.result(future, FiniteDuration(5, TimeUnit.SECONDS))
  }

}
