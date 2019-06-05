package inventory

import scala.concurrent.ExecutionContext

trait InventoryModule {
  def executionContext: ExecutionContext

  private val initialInventory = Inventory(Map(
    ProductSku("123") -> ProductStock(Product(ProductSku("123"), "Scala bar", "Tasty scala bar", 40), 30),
    ProductSku("200") -> ProductStock(Product(ProductSku("200"), "Laptop", "Some laptop", 1000), 22),
    ProductSku("300") -> ProductStock(Product(ProductSku("300"), "Carrots", "Tasty scala bar", 100), 3),
    ProductSku("400") -> ProductStock(Product(ProductSku("400"), "Tomatoes", "Tasty scala bar", 500), 100),
  ))

  lazy val inventoryManager = new InventoryManager(initialInventory)(executionContext)
}
