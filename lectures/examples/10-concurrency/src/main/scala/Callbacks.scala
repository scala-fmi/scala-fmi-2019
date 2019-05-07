import java.util.concurrent.{Executor, Executors}

case class Product(name: String, kind: String)
case class Verification(quality: Int)

object Callbacks {
  val threadPool: Executor = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors)
  def execute(work: => Unit): Unit = threadPool.execute(() => work)

  def produceProduct(onComplete: Product => Unit): Unit = execute {
    val threadId = Thread.currentThread().getId
    Thread.sleep(2000)

    println(s"Product produced, thread: $threadId")

    execute(onComplete(Product(s"Product $threadId", "Kind")))
  }
  def verifyProduct(product: Product)(onVerified: Verification => Unit): Unit = execute {
    val threadId = Thread.currentThread().getId
    Thread.sleep(2000)

    println(s"Product verified, thread: $threadId")

    execute(onVerified(Verification(threadId.hashCode)))
  }

  def produce2Products(onComplete: (Product, Product) => Unit): Unit = {
    produceProduct(???)
    produceProduct(???)

    println("Work started")
  }

  def main(args: Array[String]): Unit = execute {
    produce2Products(println(_, _))
  }

//  def produceInPipeline(callback: (List[Product], List[Verification]) => Unit): Unit = {
//    produceProduct { a =>
//      verifyProduct(a) { aVerification =>
//        produceProduct { b =>
//          verifyProduct(b) { bVerification =>
//            callback(List(a, b), List(aVerification, bVerification))
//          }
//        }
//      }
//    }
//  }
}
