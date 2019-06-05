package controllers

import inventory.{InventoryManager, ProductSku}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

class InventoryController(inventoryManager: InventoryManager,
                          cc: ControllerComponents)
                         (implicit ex: ExecutionContext) extends AbstractController(cc) {
  def inventory = Action.async {
    inventoryManager.getInventory.map(inventory => Ok(Json.toJson(inventory)))
  }

  def product(sku: ProductSku) = Action.async {
    inventoryManager.productStock(sku).map {
      case Some(productStock) => Ok(Json.toJson(productStock))
      case _ => NotFound
    }
  }
}
