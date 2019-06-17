package webclient

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.Success

object WebClientApp {
  implicit val system = ActorSystem() // an actor system and materializer are required for advenced features
  implicit val materializer = ActorMaterializer() // even if they are not used
  implicit val ec: ExecutionContext = system.dispatcher // every actor system comes with an execution context we can use

  val wsClient = StandaloneAhcWSClient()

  val userPoster = new UserPoster(wsClient)

  def main(args: Array[String]): Unit = {
    val posting = userPoster.post("zstoychev@gmail.com", 31, "Zdravko")
      .andThen { case Success(user) => println(s"User: $user") }
      .andThen { case _ => terminate() } // No matter if it's successs or error, terminate
  }

  // terminates resources and thread pools
  def terminate(): Unit = {
    wsClient.close()
    Await.result(system.terminate(), Duration.Inf)
  }
}
