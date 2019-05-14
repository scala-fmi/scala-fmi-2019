package cats

import cats.implicits._
import math.Rational

object CatsMonoidDemo extends App {
  implicit val rationalMonoid = new Monoid[Rational] {
    def empty: Rational = 0
    def combine(x: Rational, y: Rational): Rational = x + y
  }

  (2, 3) |+| (4, 5) // (6, 8)

  val map1 = Map(1 -> (2, Rational(3, 2)), 2 -> (3, Rational(4)))
  val map2 = Map(2 -> (5, Rational(6)), 3 -> (7, Rational(8, 3)))

  println(map1 |+| map2)
}
