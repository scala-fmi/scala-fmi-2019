package shopping

import inventory.InventoryAdjustment
import play.api.libs.json.Json

case class ShoppingCart(orderLines: List[OrderLine] = List.empty) {
  def add(orderLine: OrderLine) = {
    // TODO: make adding smarter
    ShoppingCart(orderLine :: orderLines)
  }

  def toInventoryAdjustment = InventoryAdjustment {
    orderLines.groupBy(_.product).mapValues(_.map(_.quantity).sum)
  }
}

object ShoppingCart {
  implicit val shoppingCartFormat = Json.format[ShoppingCart]
}
