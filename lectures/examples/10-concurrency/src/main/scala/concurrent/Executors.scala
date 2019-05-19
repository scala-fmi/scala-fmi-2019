package concurrent

import java.util.concurrent.{Executor, ForkJoinPool}

object Executors {
  implicit val defaultExecutor: Executor = new ForkJoinPool

  val currentThreadExecutor = new Executor {
    override def execute(operation: Runnable): Unit = operation.run()
  }
}
