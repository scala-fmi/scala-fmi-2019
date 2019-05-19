package math

import Monoid.ops._

object MonoidDemo extends App {
  def sum[A : Monoid](xs: List[A]) = {
    xs.foldLeft(Monoid[A].identity)(_ |+| _)
  }

  sum(List(Rational(3, 4), Rational(5), Rational(7, 4), Rational(11, 13))) // 217/26
  sum(List(Rational(1, 2), Rational(4))) // 6/8
  sum(List(1, 2, 3, 4, 5)) // 15

  {
    // We can explicitly change the context (with a new implicit val or with an import of an implicit val)

    implicit val ratMonoid = Rational.rationalMultiplicativeMonoid
    implicit val intMonoid = Monoid.intMultiplicativeMonoid

    sum(List(Rational(3, 4), Rational(5), Rational(7, 4), Rational(11, 13))) // 1155/208
    sum(List(Rational(1, 2), Rational(4))) // 2/1
    sum(List(1, 2, 3, 4, 5)) // 120
  }


  // Composite monoids:

  // Option:

  sum(List(
    Some(Rational(1)),
    None,
    Some(Rational(1, 2)),
    Some(Rational(3, 8)),
    None
  )) // Some(15/8)

  // Pairs
  (2, 3) |+| (4, 5) // (6, 8)

  val map1 = Map(1 -> (2, Rational(3, 2)), 2 -> (3, Rational(4)))
  val map2 = Map(2 -> (5, Rational(6)), 3 -> (7, Rational(8, 3)))

  // Composes Monoid[Int] and Monoid[Rational] into a pair monoid Monoid[(Int, Rational)]
  // and then composes the pair monoid into Monoid[Map[Int, (Int, Rational)]
  println(map1 |+| map2) // Map(1 -> (2,3/2), 2 -> (8,10/1), 3 -> (7,8/3))
}
