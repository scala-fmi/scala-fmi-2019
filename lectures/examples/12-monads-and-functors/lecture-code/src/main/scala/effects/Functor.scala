package effects

import scala.language.higherKinds

trait Functor[F[_]]  {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}

object Functor {
  def apply[F[_]](implicit f: Functor[F]) = f

  trait FunctorOps {
    implicit class functorOps[F[_] : Functor, A](fa: F[A]) {
      def map[B](f: A => B): F[B] = Functor[F].map(fa)(f)
    }
  }
  object ops extends FunctorOps
}
