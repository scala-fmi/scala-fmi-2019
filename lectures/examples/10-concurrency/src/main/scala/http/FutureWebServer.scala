package http

import java.util.NoSuchElementException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import util.Utils

import scala.concurrent.Future

object FutureWebServer {
  implicit val actorSystem = ActorSystem()
  implicit val ec = actorSystem.dispatcher
  implicit val materializer = ActorMaterializer()

  val routes = (path("endpoint") & get) {
    val future = Future {
      Utils.doWork
      Utils.doWork

      42
    }

    complete {
      future
        .map(result => HttpResponse(entity = result.toString))
        .recover {
          case _: NoSuchElementException => HttpResponse(StatusCodes.NotFound)
        }
    }
  }

  def main(args: Array[String]): Unit = {
    val serverBinding = Http().bindAndHandle(routes, "0.0.0.0", 9000)
  }
}
