package effects.lecture

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

trait Monad[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def unit[A](a: => A): F[A]

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = ???

//  def map[A, B](fa: F[A])(f: A => B): F[B] = ???
//  def flatten[A](mm: F[F[A]]): F[A] = flatMap(mm)(x => x) = ???
}

//object Monad {
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
//}