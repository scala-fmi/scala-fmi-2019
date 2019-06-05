import play.api.ApplicationLoader.Context
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.filters.HttpFiltersComponents

import play.api.routing.sird._

class WebAppLoader extends ApplicationLoader {
  def load(context: Context): Application = new WebApp(context).application
}

class WebApp(context: Context)
  extends BuiltInComponentsFromContext(context)
  with HttpFiltersComponents {

  lazy val router: Router = Router.empty
}
