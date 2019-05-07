package concurrent.impl

import java.util.concurrent.Executor

import concurrent.Executors

import scala.concurrent.duration.Duration
import scala.concurrent.{Awaitable, CanAwait}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

trait Future[+A] extends Awaitable[A] {
  def value: Option[Try[A]]
  def onComplete(handler: Try[A] => Unit)(implicit ex: Executor): Unit

  def isComplete: Boolean = value.isDefined

  def map[B](f: A => B)(implicit ex: Executor): Future[B] = {
    val p = Promise[B]
    onComplete(value => p.complete(value.map(f)))
    p.future
  }

  def flatMap[B](f: A => Future[B])(implicit ex: Executor): Future[B] = {
    val p = Promise[B]
    onComplete {
      case Success(value) => Future.tryF(f(value)).onComplete(p.complete)
      case Failure(e) => p.fail(e)
    }
    p.future
  }

  def zip[B](fb: Future[B]): Future[(A, B)] = {
    implicit val ex = Executors.currentThreadExecutor

    for {
      a <- this
      b <- fb
    } yield (a, b)
  }

  def map2[B, R](fb: Future[B])(f: (A, B) => R)(implicit ex: Executor): Future[R] = zip(fb).map(f.tupled)

  def filter(f: A => Boolean)(implicit ex: Executor): Future[A] = {
    val p = Promise[A]
    onComplete(value => p.complete(value.filter(f)))
    p.future
  }

  def withFilter(f: A => Boolean)(implicit ex: Executor): Future[A] = filter(f)

  def recover[B >: A](f: PartialFunction[Throwable, B])(implicit ex: Executor): Future[B] = {
    val p = Promise[B]
    onComplete(value => p.complete(value.recover(f)))
    p.future
  }

  def recoverWith[B >: A](f: PartialFunction[Throwable, Future[B]])(implicit ex: Executor): Future[B] = {
    val p = Promise[B]
    onComplete {
      case Success(value) => p succeed  value
      case Failure(e) =>
        if (f.isDefinedAt(e)) Future.tryF(f(e)) onComplete { p complete _ }
        else p.fail(e)
    }
    p.future
  }

  def foreach(f: A => Unit)(implicit ex: Executor): Unit = onComplete(_.foreach(f))
}

object Future {
  def apply[A](value: => A)(implicit ex: Executor) = {
    val p = Promise[A]
    ex.execute(() => p.succeed(value))
    p.future
  }
  def successful[A](value: A) = resolved(Success(value))
  def failed[A](e: Throwable) = resolved(Failure(e))

  def tryF[A](f: => Future[A]) = try f catch { case NonFatal(e) => Future.failed(e) }

  def resolved[A](r: Try[A]) = new Future[A] {
    val value: Option[Try[A]] = Some(r)
    def onComplete(handler: Try[A] => Unit)(implicit ex: Executor): Unit = ex.execute(() => handler(r))

    def ready(atMost: Duration)(implicit permit: CanAwait): this.type = this
    def result(atMost: Duration)(implicit permit: CanAwait): A = r.get
  }

  def firstOf[A](futures: Seq[Future[A]])(implicit ex: Executor) = {
    val p = Promise[A]
    futures.foreach(_.onComplete(p.complete))
    p.future
  }
}
