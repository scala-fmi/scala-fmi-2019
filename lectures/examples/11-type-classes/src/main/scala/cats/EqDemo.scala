package cats

import math.Rational
import cats.implicits._

object EqDemo extends App {
//  2 === ""

  implicit val rationalEq = new Eq[Rational] {
    def eqv(x: Rational, y:  Rational): Boolean = x == y
  }

  println(Rational(5) === Rational(10, 2))
  println(Rational(5, 2) =!= Rational(10, 2))
//  println(Rational(5, 2) === "")
  println(Rational(5, 2) === 2)

  case class Box[+A](a: A) {
    def contains[B >: A : Eq](b: B) = b === a
  }

  Box(1).contains(1)
//  Box(1).contains("")

  List(1).contains("")
}
