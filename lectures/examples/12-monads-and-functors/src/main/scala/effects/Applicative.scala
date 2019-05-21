package effects

import effects.Functor.FunctorOps

import scala.language.higherKinds

trait Applicative[F[_]] extends Functor[F] {
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  def unit[A](a: => A): F[A]

  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
    map(product(fa, fb)) { case (a, b) => f(a, b)}

  def product3[A, B, C](fa: F[A], fb: F[B], fc: F[C]): F[(A, B, C)] =
    map(product(product(fa, fb), fc)) { case ((a, b), c) => (a, b, c) }
  def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] =
    map(product3(fa, fb, fc)) { case (a, b, c) => f(a, b, c)}

  // and so on...

  // generalizing productN and mapN:
  def sequence[A](ml: List[F[A]]): F[List[A]] = traverse(ml)(m => m)
  def traverse[A, B](xs: List[A])(f: A => F[B]): F[List[B]] = {
    xs.foldRight(unit(List[B]())) { (next, acc) =>
      map2(f(next), acc)(_ :: _)
    }
  }
}

object Applicative {
  def apply[F[_]](implicit f: Applicative[F]) = f

  trait ApplicativeOps extends FunctorOps
}
