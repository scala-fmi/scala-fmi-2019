package example

import scala.concurrent.{ExecutionContext, Future}
import scala.language.{higherKinds, implicitConversions, reflectiveCalls}

trait Monad[F[+ _]] {
  def unit[A](a: => A): F[A]

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(a => unit(f(a)))

  def join[A](ffa: F[F[A]]): F[A] = flatMap(ffa)(identity)

  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] =
    flatMap(fa)(a => map(fb)(b => f(a, b)))
}

object Monad {
  def apply[F[+ _]](implicit monadInstance: Monad[F]): Monad[F] = monadInstance
}

object MonadInstances {

  implicit class MonadOps[F[+ _] : Monad, A](fa: F[A]) {
    def flatMap[B](f: A => F[B]): F[B] = Monad[F].flatMap(fa)(f)

    def map[B](f: A => B): F[B] = Monad[F].map(fa)(f)

    def join[B](implicit ev: F[A] =:= F[F[B]]): F[B] = Monad[F].join[B](fa)

    def map2[B, C](fb: F[B])(f: (A, B) => C): F[C] = Monad[F].map2(fa, fb)(f)
  }

  implicit val optionInstance: Monad[Option] = new Monad[Option] {
    override def unit[A](a: => A): Option[A] = Some(a)

    override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
      fa.flatMap(f)
  }

  implicit val listInstance: Monad[List] = new Monad[List] {
    override def unit[A](a: => A): List[A] = List(a)

    override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] =
      fa.flatMap(f)
  }

  implicit def eitherInstance[S]: Monad[({type T[+A] = Either[S, A]})#T] =
    new Monad[({type T[+A] = Either[S, A]})#T] {
      override def unit[A](a: => A): Either[S, A] = Right(a)

      override def flatMap[A, B](fa: Either[S, A])(f: A => Either[S, B]): Either[S, B] =
        fa.flatMap(f)
    }

  implicit def futureInstance(implicit ex: ExecutionContext): Monad[Future] =
    new Monad[Future] {
      override def unit[A](a: => A): Future[A] = Future.successful(a)

      override def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] =
        fa.flatMap(f)
    }

  implicit def optionTransformerInstance[F[+ _] : Monad]: Monad[({type T[+A] = OptionT[F, A]})#T] =
    new Monad[({type T[+A] = OptionT[F, A]})#T] {
      override def unit[A](a: => A): OptionT[F, A] = OptionT(Monad[F].unit(Some(a)))

      override def map[A, B](fa: OptionT[F, A])(f: A => B): OptionT[F, B] =
        OptionT(fa.run.map(_.map(f)))

      override def join[A](ffa: OptionT[F, OptionT[F, A]]): OptionT[F, A] =
        OptionT(ffa.run.map(_.map(_.run).traverseM(identity)).flatMap(identity))

      override def flatMap[A, B](fa: OptionT[F, A])(f: A => OptionT[F, B]): OptionT[F, B] =
        join(map(fa)(f))
    }

  implicit def eitherTransformerInstance[F[+ _] : Monad, S]: Monad[({type T[+A] = EitherT[F, S, A]})#T] =
    new Monad[({type T[+A] = EitherT[F, S, A]})#T] {
      override def unit[A](a: => A): EitherT[F, S, A] = EitherT(Monad[F].unit(Right(a)))

      override def map[A, B](fa: EitherT[F, S, A])(f: A => B): EitherT[F, S, B] =
        EitherT(fa.run.map(_.map(f)))

      override def join[A](ffa: EitherT[F, S, EitherT[F, S, A]]): EitherT[F, S, A] =
        EitherT(ffa.run.map(_.map(_.run).traverseM(identity)).flatMap(identity))

      override def flatMap[A, B](fa: EitherT[F, S, A])(f: A => EitherT[F, S, B]): EitherT[F, S, B] =
        join(map(fa)(f))
    }

  implicit class ListSequence[F[+ _] : Monad, A](list: List[F[A]]) {
    def sequence: F[List[A]] =
      list.foldRight[F[List[A]]](Monad[F].unit(Nil))((next, acc) => Monad[F].map2(next, acc)(_ :: _))
  }

  implicit class OptionSequence[F[+ _] : Monad, A](option: Option[F[A]]) {
    def sequence: F[Option[A]] = option match {
      case Some(fa) => fa.map(Some(_))
      case None => Monad[F].unit(None)
    }
  }

  implicit class EitherSequence[F[+ _] : Monad, S, A](either: Either[S, F[A]]) {
    def sequence: F[Either[S, A]] = either match {
      case Right(fa) => fa.map(Right(_))
      case Left(s) => Monad[F].unit(Left(s))
    }
  }

  implicit class ListTraverse[A](list: List[A]) {
    def traverse[F[+ _] : Monad, B](f: A => F[B]): F[List[B]] = list.map(f).sequence
  }

  implicit class OptionTraverse[A](op: Option[A]) {
    def traverse[F[+ _] : Monad, B](f: A => F[B]): F[Option[B]] = op.map(f).sequence
  }

  implicit class OptionTraverseM[A](op: Option[A]) {
    def traverseM[F[+ _] : Monad, B](f: A => F[Option[B]]): F[Option[B]] = op.traverse(f).map(_.flatten)
  }

  implicit class EitherTraverse[S, A](either: Either[S, A]) {
    def traverse[F[+ _] : Monad, B](f: A => F[B]): F[Either[S, B]] = either.map(f).sequence
  }

  implicit class EitherTraverseM[S, A](either: Either[S, A]) {
    def traverseM[F[+ _] : Monad, B](f: A => F[Either[S, B]]): F[Either[S, B]] =
      either.traverse(f).map(_.flatMap(identity))
  }

}

class OptionT[F[+ _] : Monad, +A](val run: F[Option[A]]) {
  def mapOption[B](f: Option[A] => Option[B]): OptionT[F, B] =
    OptionT(Monad[F].map(run)(f))
}

object OptionT {

  import MonadInstances._

  def apply[F[+ _] : Monad, A](run: F[Option[A]]): OptionT[F, A] = new OptionT(run)

  implicit class OptionTOps[F[+ _] : Monad, +A](optionT: OptionT[F, A]) {
    type T[+X] = OptionT[F, X]

    def map[B](f: A => B): T[B] = Monad[T].map(optionT)(f)

    def flatMap[B](f: A => T[B]): T[B] = Monad[T].flatMap(optionT)(f)

    def map2[B, C](fb: T[B])(f: (A, B) => C): T[C] = Monad[T].map2(optionT, fb)(f)
  }

}

class EitherT[F[+ _] : Monad, S, +A](val run: F[Either[S, A]]) {
  def mapEither[B](f: Either[S, A] => Either[S, B]): EitherT[F, S, B] =
    EitherT(Monad[F].map(run)(f))
}

object EitherT {

  import MonadInstances._

  def apply[F[+ _] : Monad, S, A](run: F[Either[S, A]]): EitherT[F, S, A] = new EitherT(run)

  implicit class EitherTOps[F[+ _] : Monad, S, +A](eitherT: EitherT[F, S, A]) {
    type T[+X] = EitherT[F, S, X]

    def map[B](f: A => B): T[B] = Monad[T].map(eitherT)(f)

    def flatMap[B](f: A => T[B]): T[B] = Monad[T].flatMap(eitherT)(f)

    def map2[B, C](fb: T[B])(f: (A, B) => C): T[C] = Monad[T].map2(eitherT, fb)(f)
  }

}

