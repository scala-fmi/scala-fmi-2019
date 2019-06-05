package inventory

import scala.concurrent.{ExecutionContext, Future}

class InventoryManager(startingInventory: Inventory)
                      (implicit ex: ExecutionContext) {
  @volatile private var inventory = startingInventory

  def getInventory: Future[Inventory] = Future {
    inventory
  }

  def productStock(sku: ProductSku): Future[Option[ProductStock]] = Future {
    inventory.productsStock.get(sku)
  }

  def applyAdjustment(inventoryAdjustment: InventoryAdjustment): Future[AdjustmentResult] = Future {
    this.synchronized {
      inventory.adjust(inventoryAdjustment) match {
        case Some(newInventory) =>
          inventory = newInventory
          SuccessfulAdjustment
        case None => NotEnoughQuantity
      }
    }
  }
}

sealed trait AdjustmentResult
case object SuccessfulAdjustment extends AdjustmentResult
case object NotEnoughQuantity extends AdjustmentResult