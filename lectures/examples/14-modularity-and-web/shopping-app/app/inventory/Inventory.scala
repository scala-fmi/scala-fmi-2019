package inventory

import play.api.libs.json.{JsValue, Json, Writes}

case class ProductStock(product: Product, quantity: Int) {
  def adjustWith(n: Int) = ProductStock(product, quantity + n)
}

case class Inventory(productsStock: Map[ProductSku, ProductStock]) {
  def isAdjustmentApplicable(inventoryAdjustment: InventoryAdjustment): Boolean = {
    inventoryAdjustment.adjustments.forall {
      case (product, quantityAdjustment) =>
        val availableQuantity = productsStock.get(product).map(_.quantity).getOrElse(0)

        availableQuantity + quantityAdjustment >= 0
    }
  }

  def adjust(inventoryAdjustment: InventoryAdjustment): Option[Inventory] = {
    if (isAdjustmentApplicable(inventoryAdjustment)) Some {
      Inventory {
        inventoryAdjustment.adjustments.foldLeft(productsStock) { (inventory, adjustment) =>
          val (product, quantity) = adjustment

          inventory + (product -> inventory(product).adjustWith(quantity))
        }
      }
    }
    else None
  }
}

object ProductStock {
  implicit val productStockFormat = Json.format[ProductStock]
}

object Inventory {
  implicit val inventoryWrites = new Writes[Inventory] {
    def writes(inventory: Inventory): JsValue = Json.toJson(inventory.productsStock.values)
  }
}
