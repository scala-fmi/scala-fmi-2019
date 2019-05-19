package concurrent.lecture

import java.util.concurrent.Executor

import concurrent.Executors

import scala.util.{Failure, Success, Try}

// The implementation from the lecture
trait Future[+A] {
  def value: Option[Try[A]]
  def isCompleted = value.isDefined

  def onComplete(f: Try[A] => Any)(implicit ex: Executor): Unit
  def result: Try[A]

  def map[B](f: A => B)(implicit ex: Executor): Future[B] = {
    val p = Promise[B]

    onComplete(result => {
      val newValue = result.map(f)
      p.complete(newValue)
    })

    p.future
  }
  def flatMap[B](f: A => Future[B])(implicit ex: Executor): Future[B] = {
    val p = Promise[B]
    onComplete {
      case Success(value) =>
        Try(f(value)).fold(e => Future.failed(e), identity).onComplete(p.complete)
      case Failure(e) => p.fail(e)
    }
    p.future
  }


  def filter(f: A => Boolean)(implicit ex: Executor): Future[A] = {
    val p = Promise[A]
    onComplete(value => p.complete(value.filter(f)))
    p.future
  }

  // required for for comprehensions's if and pattern matching uses
  def withFilter(f: A => Boolean)(implicit ex: Executor): Future[A] = filter(f)

  def zip[B](fb: Future[B]): Future[(A, B)] = {
    implicit val ex = Executors.currentThreadExecutor

    for {
      a <- this
      b <- fb
    } yield (a, b)
  }

  def zipMap[B, R](fb: Future[B])(f: (A, B) => R)(implicit ex: Executor): Future[R] = this.zip(fb).map(f.tupled)

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
        if (f.isDefinedAt(e))
          Try(f(e)).fold(e => Future.failed(e), identity).onComplete(p.complete)
        else p.fail(e)
    }
    p.future
  }
}

object Future {
  def apply[A](value: => A)(implicit ex: Executor): Future[A] = {
    val p = Promise[A]

    ex.execute(() => {
      p.complete(Try(value))
    })

    p.future
  }

  def successful[A](value: A) = resolved(Success(value))
  def failed[A](e: Throwable) = resolved(Failure(e))

  def resolved[A](r: Try[A]) = new Future[A] {
    val value: Option[Try[A]] = Some(r)
    def onComplete(f: Try[A] => Any)(implicit ex: Executor): Unit = ex.execute(() => f(r))
    def result: Try[A] = r
  }
}

object FutureApp extends App {
  import concurrent.Executors.defaultExecutor

  def double(n: Int): Future[Int] = Future {
    n * 2
  }

  val calc1 = Future { 1 + 1 }
  val calc2 = Future { 42 * 20 }

  val composed = for {
    (a, b) <- calc1.zip(calc2)
    c <- double(a + b)
  } yield c.toString

  println(composed.result)
}
