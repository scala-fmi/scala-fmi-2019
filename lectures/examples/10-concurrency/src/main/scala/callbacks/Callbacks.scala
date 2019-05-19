package callbacks

import java.util.concurrent.{Executor, Executors}

case class Product(name: String, kind: String)
case class Verification(quality: Int)

object Callbacks {
  val threadPool: Executor = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors)
  def execute(work: => Any): Unit = threadPool.execute(() => work)

  def produceProduct(onComplete: Product => Unit): Unit = execute {
    val productId = Thread.currentThread().getId

    println(s"Producing product $productId...")
    Thread.sleep(2000)
    println(s"Product produced, $productId")

    execute(onComplete(Product(s"Product $productId", "Kind")))
  }

  def produce2Products(onComplete: (Product, Product) => Unit): Unit = {
    var firstProduct: Option[Product] = None

    // Needs mutability and manual synchronization
    def callback(product: Product): Unit = this.synchronized {
      firstProduct match {
        case Some(first) => onComplete(first, product)
        case None => firstProduct = Some(product)
      }
    }

    produceProduct(callback)
    produceProduct(callback)

    println("Work started")
  }

  def main(args: Array[String]): Unit = execute {
    produce2Products(println(_, _))
  }

  def verifyProduct(product: Product)(onVerified: Verification => Unit): Unit = execute {
    val threadId = Thread.currentThread().getId

    println(s"Verifying product ${product.name}...")
    Thread.sleep(2000)
    println(s"Product verified, thread: $threadId")

    execute(onVerified(Verification(threadId.hashCode)))
  }

  def produceInPipeline(callback: (List[Product], List[Verification]) => Unit): Unit = {
    // Callback hell
    produceProduct { a =>
      verifyProduct(a) { aVerification =>
        produceProduct { b =>
          verifyProduct(b) { bVerification =>
            callback(List(a, b), List(aVerification, bVerification))
          }
        }
      }
    }
  }
}
