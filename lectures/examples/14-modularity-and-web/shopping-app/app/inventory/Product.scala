package inventory

import play.api.libs.json._

case class ProductSku(sku: String)
case class Product(sku: ProductSku, name: String, description: String, weightInGrams: Int)

object ProductSku {
  implicit val productSkuFormat = new Format[ProductSku] {
    def writes(o: ProductSku): JsValue = JsString(o.sku)
    def reads(json: JsValue): JsResult[ProductSku] = json.validate[String].map(ProductSku(_))
  }
}

object Product {
  implicit val productFormat = Json.format[Product]
}