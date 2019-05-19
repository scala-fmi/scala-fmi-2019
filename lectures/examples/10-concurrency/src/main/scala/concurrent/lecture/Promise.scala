package concurrent.lecture

import java.util.concurrent.Executor
import java.util.concurrent.locks.LockSupport

import concurrent.Executors

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

class Promise[A] { self => // this allows us to rename the "this" reference so that we can use it correctly in the internal Future object bellow
  case class Handler(handler: Try[A] => Any, ex: Executor) {
    def executeWithValue(value: Try[A]): Unit = ex.execute(() => handler(value))
  }

  sealed trait State
  case class Completed(value: Try[A]) extends State
  case class Pending(handlers: List[Handler]) extends State

  @volatile private var state: State = Pending(List.empty)

  def complete(value: Try[A]): Unit = {
    val pendingHandlers = self.synchronized {
      state match {
        case Completed(_) => List.empty
        case Pending(handlers) =>
          state = Completed(value)
          handlers
      }
    }

    // execute them outside the lock
    pendingHandlers.foreach(_.executeWithValue(value))
  }

  def succeed(value: A): Unit = complete(Success(value))
  def fail(ex: Throwable): Unit = complete(Failure(ex))

  def future: Future[A] = new Future[A] {
    def onComplete(f: Try[A] => Any)
                           (implicit ex: Executor): Unit = {
      val newHandler = Handler(f, ex)

      val toBeExecutedWith: Option[Try[A]] = self.synchronized {
        state match {
          case Completed(value) => Some(value)
          case Pending(handlers) =>
            state = Pending(newHandler :: handlers)
            None
        }
      }

      // execute outside the lock if the state was completed
      toBeExecutedWith.foreach(newHandler.executeWithValue)
    }

    def value: Option[Try[A]] = state match {
      case Completed(value) => Some(value)
      case _ => None
    }

    @tailrec
    def result: Try[A] = state match {
      case Completed(value) => value
      case _ =>
        val thread = Thread.currentThread()

        onComplete { _ =>
          // unpark will wake the thread if it's parked. Otherwise its next call to park will complete right away
          LockSupport.unpark(thread)
        }(Executors.currentThreadExecutor)

        // The tread will be suspended until another threads calls `unpark`, i.e. the future is completed
        LockSupport.park()

        result
    }
  }
}

object Promise{
  def apply[A]: Promise[A] = new Promise[A]
}

object PromiseApp {
  def doHttpCall(url: String): Future[Int] = {
    val p = Promise[Int]

    // doing http call
    // ...

    // after finished
    p.complete(Try(42))

    p.future
  }
}
