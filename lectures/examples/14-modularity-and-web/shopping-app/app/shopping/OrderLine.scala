package shopping

import inventory.ProductSku
import play.api.libs.json.Json

case class OrderLine(product: ProductSku, quantity: Int)

object OrderLine {
  implicit val orderLineFormat = Json.format[OrderLine]
}