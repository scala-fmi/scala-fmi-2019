package shopping

import inventory.InventoryManager

import scala.concurrent.ExecutionContext

trait ShoppingModule {
  def executionContext: ExecutionContext
  def inventoryManager: InventoryManager

  lazy val shop = new Shop(inventoryManager)(executionContext)
}
