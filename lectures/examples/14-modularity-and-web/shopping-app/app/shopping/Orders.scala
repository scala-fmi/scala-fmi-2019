package shopping

import scala.collection.immutable.Queue

case class Orders(orders: Queue[Order]) {
  def placeNew(order: Order) = Orders(orders.enqueue(order))
}