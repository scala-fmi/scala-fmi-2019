import java.util.concurrent.{Executor, Executors}

case class Product(name: String, kind: String)
case class Verification(quality: Int)

object Callbacks extends App {
  val threadPool: Executor = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors)
  def execute(work: => Unit): Unit = threadPool.execute(() => work)

  def produceProduct(callback: Product => Unit): Unit = ???
  def verifyProduct(product: Product)(callback: Verification => Unit): Unit = ???

  def produce2Products(callback: (Product, Product) => Unit): Unit = {
    execute(produceProduct(???))
    execute(produceProduct(???))
  }

  execute {
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
