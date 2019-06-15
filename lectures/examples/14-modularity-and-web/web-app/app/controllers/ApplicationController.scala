package controllers

import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

class ApplicationController(cc: ControllerComponents,
                            wsClient: WSClient)
                           (implicit ec: ExecutionContext)extends AbstractController(cc) {
  // Can be both def or val
  def index = Action { request =>
    Ok("Hello World!")
  }

  def retrieveWebPage(url: String) = Action.async { request =>
    wsClient.url(url).get().map { urlResponse =>
      Ok(urlResponse.body).as(urlResponse.contentType) // .as changes the content type
    } recover {
      case NonFatal(e) => ServiceUnavailable(s"Couldn't retrieve $url")
    }
  }
}
