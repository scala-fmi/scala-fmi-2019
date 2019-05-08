package concurrent.impl

import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.LockSupport

import concurrent.Executors

import scala.annotation.tailrec
import scala.concurrent.duration.Duration
import scala.concurrent.{CanAwait, TimeoutException}
import scala.util.{Failure, Success, Try}

class Promise[A] {
  case class Handler(handler: Try[A] => Any, ex: Executor) {
    def executeWithValue(value: Try[A]) = ex.execute(() => handler(value))
  }

  sealed trait State
  case class Completed(value: Try[A]) extends State
  case class Pending(handlers: List[Handler]) extends State

  private val state = new AtomicReference[State](Pending(List.empty))

  @tailrec
  private def executeWhenComplete(handler: Handler): Unit = state.get() match {
    case Completed(value) => handler.executeWithValue(value)
    case s @ Pending(handlers) =>
      val newState = Pending(handler :: handlers)
      if (!state.compareAndSet(s, newState)) executeWhenComplete(handler)
  }

  @tailrec
  private def completeWithValue(value: Try[A]): List[Handler] = state.get() match {
    case Completed(_) => List.empty
    case s @ Pending(handlers) =>
      if (state.compareAndSet(s, Completed(value))) handlers
      else completeWithValue(value)
  }

  val future: Future[A] = new Future[A] {
    def value: Option[Try[A]] = state.get() match {
      case Completed(value) => Some(value)
      case _ => None
    }

    def onComplete(handler: Try[A] => Unit)(implicit ex: Executor): Unit = executeWhenComplete(Handler(handler, ex))

    def ready(atMost: Duration)(implicit permit: CanAwait): this.type = {
      if (!isComplete && Duration.Zero < atMost) {
        val thread = Thread.currentThread
        onComplete(_ => LockSupport.unpark(thread))(Executors.currentThreadExecutor)

        if (atMost == Duration.Inf) LockSupport.park()
        else LockSupport.parkNanos(atMost.toNanos)
      }

      if (isComplete) this
      else throw new TimeoutException
    }

    def result(atMost: Duration)(implicit permit: CanAwait): A = ready(atMost).value.get.get
  }

  def complete(value: Try[A]): Promise[A] = {
    completeWithValue(value).foreach(_.executeWithValue(value))
    this
  }

  def succeed(value: A): Promise[A] = complete(Success(value))

  def fail(e: Throwable): Promise[A] = complete(Failure(e))
}

object Promise {
  def apply[T] = new Promise[T]
}
