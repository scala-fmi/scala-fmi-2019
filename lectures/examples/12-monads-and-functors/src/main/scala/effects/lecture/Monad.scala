package effects.lecture

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

trait Monad[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def unit[A](a: => A): F[A]

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = ???

//  def map[A, B](fa: F[A])(f: A => B): F[B] = ???
//  def flatten[A](mm: F[F[A]]): F[A] = ???

//  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] = flatMap(fa)(a => map(fb)(b => (a, b)))
//  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = map(zip(fa, fb)) { case (a, b) => f(a, b) }
//  def sequence[A](xs: List[F[A]]): F[List[A]] = {
//    xs.foldRight(unit(List[A]())) { (next, acc) =>
//      map2(next, acc)(_ :: _)
//    }
//  }
}

object Monad {
//  implicit val optionMonad = new Monad[Option] {
//    def flatMap[A, B](m: Option[A])(f: A => Option[B]): Option[B] = m flatMap f
//    def unit[A](a: => A): Option[A] = Some(a)
//  }
//
//  implicit val tryMonad = new Monad[Try] {
//    def flatMap[A, B](m: Try[A])(f: A => Try[B]): Try[B] = m flatMap f
//    def unit[A](a: => A): Try[A] = Success(a)
//  }
//
//  implicit val listMonad = new Monad[List] {
//    def flatMap[A, B](m: List[A])(f: A => List[B]): List[B] = m flatMap f
//    def unit[A](a: => A): List[A] = List(a)
//  }
//
//  implicit def futureMonad(implicit ec: ExecutionContext) = new Monad[Future] {
//    def flatMap[A, B](m: Future[A])(f: A => Future[B]): Future[B] = m flatMap f
//    def unit[A](a: => A): Future[A] = Future(a)
//  }

//  def apply[F[_]](implicit m: Monad[F]) = m
//
//  object ops {
//    implicit class MonadOps[F[_] : Monad, A](fa: F[A]) {
//      def map[B](f: A => B) = Monad[F].map(fa)(f)
//      def flatMap[B](f: A => F[B]) = Monad[F].flatMap(fa)(f)
//      def flatten[B](implicit ev: A =:= F[B]): F[B] = {
//        val ffb = Monad[F].map(fa)(ev)
//        Monad[F].flatten(ffb)
//      }
//    }
//
//    implicit class ListWithMonadOps[F[_] : Monad, A](faxs: List[F[A]]) {
//      def sequence = Monad.sequence(faxs)
//    }
//  }

//  def compose[A, B, C, F[_]](f: A => F[B], g: B => F[C])(implicit m: Monad[F]) = m.compose(f, g)
//  def map2[A, B, C, F[_]](ma: F[A], mb: F[B])(f: (A, B) => C)(implicit m: Monad[F]) = m.map2(ma, mb)(f)
//  def sequence[A, F[_]](ml: List[F[A]])(implicit m: Monad[F]) = m.sequence(ml)
}

object MonadDemo extends App {
//  import Monad.ops._
//
//  val opt1: Option[Int] = Some(42)
//  val opt2: Option[Int] = Some(10)
//
//  Monad.map2(opt1, opt2)(_ + _)
//
//  val listOfOptions = List(Some(1), None, Some(2))
//
//  println(
//    Monad.sequence(listOfOptions),
//    listOfOptions.sequence
//  )
}