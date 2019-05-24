package effects

import effects.Monad.MonadOps

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

trait Monad[F[_]] extends Functor[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def unit[A](a: => A): F[A]

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = a => flatMap(f(a))(g)

  def map[A, B](m: F[A])(f: A => B): F[B] = flatMap(m)(a => unit(f(a)))
  def flatten[A](mm: F[F[A]]): F[A] = flatMap(mm)(x => x)

  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] = flatMap(fa)(a => map(fb)(b => (a, b)))
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = map(zip(fa, fb)) { case (a, b) => f(a, b) }

  def sequence[A](xs: List[F[A]]): F[List[A]] = {
    xs.foldRight(unit(List[A]())) { (next, acc) =>
      map2(next, acc)(_ :: _)
    }
  }
}

object Monad {
  def apply[F[_]](implicit m: Monad[F]) = m

  def map[A, B, F[_]](ma: F[A])(f: A => B)(implicit m: Monad[F]) = m.map(ma)(f)
  def flatMap[A, B, F[_]](ma: F[A])(f: A => F[B])(implicit m: Monad[F]) = m.flatMap(ma)(f)

  def compose[A, B, C, F[_]](f: A => F[B], g: B => F[C])(implicit m: Monad[F]) = m.compose(f, g)
  def zip[A, B, F[_]](ma: F[A], mb: F[B])(implicit m: Monad[F]) = m.zip(ma, mb)
  def map2[A, B, C, F[_]](ma: F[A], mb: F[B])(f: (A, B) => C)(implicit m: Monad[F]) = m.map2(ma, mb)(f)
  def sequence[A, F[_]](ml: List[F[A]])(implicit m: Monad[F]) = m.sequence(ml)

  trait MonadOps {
    implicit class MonadOps[F[_] : Monad, A](fa: F[A]) {
      def map[B](f: A => B) = Monad[F].map(fa)(f)
      def flatMap[B](f: A => F[B]) = Monad[F].flatMap(fa)(f)
      def flatten[B](implicit ev: A =:= F[B]): F[B] = {
        val ffb = Monad[F].map(fa)(ev)
        Monad[F].flatten(ffb)
      }
    }

    implicit class ListWithMonadOps[F[_] : Monad, A](faxs: List[F[A]]) {
      def sequence = Monad.sequence(faxs)
    }
  }
  object ops extends MonadOps

  // Implementing monads from the Scala library
  implicit val optionMonad = new MonadFilter[Option] {
    def flatMap[A, B](m: Option[A])(f: A => Option[B]): Option[B] = m flatMap f
    def unit[A](a: => A): Option[A] = Some(a)
    def mzero[A]: Option[A] = None

    override def map[A, B](m: Option[A])(f: A => B): Option[B] = m map f
  }

  implicit val tryMonad = new MonadFilter[Try] {
    def flatMap[A, B](m: Try[A])(f: A => Try[B]): Try[B] = m flatMap f
    def unit[A](a: => A): Try[A] = Success(a)
    def mzero[A]: Try[A] = Failure(new NoSuchElementException)

    override def map[A, B](m: Try[A])(f: A => B): Try[B] = m map f
  }

  implicit val listMonad = new MonadFilter[List] {
    def flatMap[A, B](m: List[A])(f: A => List[B]): List[B] = m flatMap f
    def unit[A](a: => A): List[A] = List(a)
    def mzero[A]: List[A] = Nil

    override def map[A, B](m: List[A])(f: A => B): List[B] = m map f
  }

  implicit def futureMonad(implicit ec: ExecutionContext) = new MonadFilter[Future] {
    def flatMap[A, B](m: Future[A])(f: A => Future[B]): Future[B] = m flatMap f
    def unit[A](a: => A): Future[A] = Future(a)
    def mzero[A]: Future[A] = Future.failed(new NoSuchElementException)

    override def map[A, B](m: Future[A])(f: A => B): Future[B] = m map f
  }

  implicit def eitherMonad[E] = new Monad[({type T[A] = Either[E, A]})#T] {
    def flatMap[A, B](fa:  Either[E, A])(f:  A => Either[E, B]): Either[E, B] = fa.flatMap(f)
    def unit[A](a: => A): Either[E, A] = Right(a)
  }
}

trait MonadFilter[F[_]] extends Monad[F] {
  def mzero[A]: F[A]
  def filter[A](fa: F[A])(f: A => Boolean): F[A] = flatMap(fa) { x => if (f(x)) unit(x) else mzero }
}

object MonadFilter {
  def apply[F[_]](implicit m: MonadFilter[F]) = m

  trait MonadFilterOps extends MonadOps {
    implicit class MonadFilterOps[F[_] : MonadFilter, A](fa: F[A]) {
      def filter(f: A => Boolean) = MonadFilter[F].filter(fa)(f)
    }
  }
  object ops extends MonadFilterOps
}
