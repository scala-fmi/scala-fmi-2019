import authentication.AuthenticatedAction
import controllers.{ApplicationController, InventoryController, ShoppingController, UserController}
import inventory.{InventoryModule, ProductSku}
import play.api.ApplicationLoader.Context
import play.api.mvc.BodyParsers
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.filters.HttpFiltersComponents
import shopping.ShoppingModule
import user.UsersModule

class ShoppingAppLoader extends ApplicationLoader {
  def load(context: Context): Application = new ShoppingApp(context).application
}

class ShoppingApp(context: Context) extends BuiltInComponentsFromContext(context)
  with HttpFiltersComponents
  with UsersModule
  with InventoryModule
  with ShoppingModule {

  lazy val defaultBodyParsers = new BodyParsers.Default(playBodyParsers)

  lazy val authenticationAction = new AuthenticatedAction(registeredUsersRepository, defaultBodyParsers)

  lazy val applicationController = new ApplicationController(controllerComponents)
  lazy val userController = new UserController(controllerComponents, registeredUsersRepository, authenticationAction)
  lazy val inventoryController = new InventoryController(inventoryManager, controllerComponents)
  lazy val shoppingController = new ShoppingController(shop, controllerComponents, authenticationAction)

  lazy val router: Router = Router.from {
    case GET(p"/") => applicationController.index

    case GET(p"/current-user") => userController.user
    case POST(p"/login") => userController.login
    case POST(p"/logout") => userController.logout
    case POST(p"/users") => userController.register

    case GET(p"/products") => inventoryController.inventory
    case GET(p"/products/$sku") => inventoryController.product(ProductSku(sku))

    case GET(p"/current-user/cart") => shoppingController.cart
    case POST(p"/current-user/cart") => shoppingController.addToCart
    case POST(p"/current-user/cart/order") => shoppingController.placeOrder
  }
}
