package effects.lecture

import scala.language.higherKinds

trait Functor[F[_]]  {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
