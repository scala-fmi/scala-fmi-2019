package shopping

import java.time.Instant

import inventory.{InventoryManager, NotEnoughQuantity, SuccessfulAdjustment}
import user.User

import scala.collection.immutable.Queue
import scala.concurrent.{ExecutionContext, Future}

class Shop(inventoryManager: InventoryManager)
          (implicit executionContext: ExecutionContext) {
  @volatile private var orders = Orders(Queue.empty)

  private def addOrder(order: Order): Future[Order] = Future {
    this.synchronized {
      orders = orders.placeNew(order)

      order
    }
  }

  def placeOrder(user: User, shoppingCart: ShoppingCart): Future[Order] = {
    inventoryManager.applyAdjustment(shoppingCart.toInventoryAdjustment).flatMap {
      case SuccessfulAdjustment =>
        val order = Order(user.email, shoppingCart.orderLines, Instant.now())

        addOrder(order)
      case NotEnoughQuantity => Future.failed(NotEnoughQuantityForOrder)
    }
  }
}

case object NotEnoughQuantityForOrder extends Exception
