package shopping

import java.time.Instant

import play.api.libs.json.Json

case class Order(user: String, orderLines: List[OrderLine], instant: Instant)

object Order {
  implicit val orderFormat = Json.format[Order]
}