package effects.maybe

import effects.Monad
import Monad.ops._

trait Maybe[+A]
case class Just[+A](a: A) extends Maybe[A]
case object Nthng extends Maybe[Nothing]

object Maybe {
  implicit val maybeMonad = new Monad[Maybe] {
    def flatMap[A, B](fa:  Maybe[A])(f: A => Maybe[B]): Maybe[B] = fa match {
      case Just(a) => f(a)
      case _ => Nthng
    }

    def unit[A](a:  =>A): Maybe[A] = Just(a)
  }
}

object MaybeDemo extends App {
  def f(n: Int): Maybe[Int] = Just(n + 1)
  def g(n: Int): Maybe[Int] = Just(n * 2)
  def h(n: Int): Maybe[Int] = Just(n * n)

  val a = 3

  val result = for {
    b <- f(a)
    c <- g(b * 2)
    d <- h(b + c)
  } yield a * b * d
  println(result)



  val maybe1: Maybe[Int] = Just(42)
  val maybe2: Maybe[Int] = Just(10)

  Monad.map2(maybe1, maybe2)(_ + _)

  val listOfMaybes = List(Just(1), Nthng, Just(2))

  println(
    Monad.sequence(listOfMaybes),
    listOfMaybes.sequence
  )
}
