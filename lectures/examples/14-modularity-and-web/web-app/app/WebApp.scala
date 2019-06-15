import controllers.{ApplicationController, UsersController}
import play.api.ApplicationLoader.Context
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.{EssentialFilter, Results}
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.filters.HttpFiltersComponents

class WebAppLoader extends ApplicationLoader {
  def load(context: Context): Application = new WebApp(context).application
}

class WebApp(context: Context)
  extends BuiltInComponentsFromContext(context)
  with HttpFiltersComponents
  with AhcWSComponents {

  override def httpFilters: Seq[EssentialFilter] = Seq.empty // disables filters from HttpFiltersComponents, like CSRF

  lazy val applicationController = new ApplicationController(controllerComponents, wsClient)
  lazy val usersController = new UsersController(controllerComponents)

  val mainRoutes: Router.Routes = {
    case GET(p"/") => applicationController.index

    case GET(p"/web-page/$domain") => applicationController.retrieveWebPage(s"http://$domain")
  }

  val userRoutes: Router.Routes = {
    case GET(p"/current-user") => usersController.getUser
    case POST(p"/current-user") => usersController.postUser

    case GET(p"/users/$id") => Action {
      Results.Ok(id)
    }
  }

  lazy val router: Router = Router.from(mainRoutes orElse userRoutes)
}
