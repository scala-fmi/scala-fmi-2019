package controllers

import authentication.AuthenticatedAction
import controllers.ShoppingController.ShoppingCartSessionKey
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents, Session}
import shopping.{NotEnoughQuantityForOrder, OrderLine, Shop, ShoppingCart}

import scala.concurrent.ExecutionContext

class ShoppingController(shop: Shop,
                         cc: ControllerComponents,
                         authenticatedAction: AuthenticatedAction)
                        (implicit ex: ExecutionContext) extends AbstractController(cc) {
  def addToCart = authenticatedAction(parse.json[OrderLine]) { request =>
    val cart = getShoppingCart(request.session)
    val orderLine = request.body

    val newCart = cart.add(orderLine)
    val newCartJson = Json.toJson(newCart)

    Ok(newCartJson).withSession(ShoppingCartSessionKey -> newCartJson.toString)
  }

  def cart = authenticatedAction { request =>
    Ok(Json.toJson(getShoppingCart(request.session)))
  }

  def placeOrder = authenticatedAction.async { request =>
    val cart = getShoppingCart(request.session)

    shop.placeOrder(???, cart)
      .map(order => Ok(Json.toJson(order)))
      .recover {
        case NotEnoughQuantityForOrder => Conflict
      }
  }

  def getShoppingCart(session: Session): ShoppingCart = {
    session.get(ShoppingCartSessionKey)
      .map(Json.parse(_).validate[ShoppingCart])
      .flatMap(_.asOpt)
      .getOrElse(ShoppingCart())
  }
}

object ShoppingController {
  val ShoppingCartSessionKey = "shopping-cart"
}
