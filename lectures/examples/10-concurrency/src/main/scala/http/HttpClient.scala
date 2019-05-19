package http

import concurrent.impl.{Future, Promise}
import org.asynchttpclient.Dsl._
import org.asynchttpclient._

import scala.util.Try

object HttpClient {
  val client = asyncHttpClient()

  def get(url: String): Future[Response] = {
    val p = Promise[Response]

    val response = client.prepareGet(url).setFollowRedirect(true).execute()
    response.addListener(() => p.complete(Try(response.get())), null)

    p.future
  }

  def getScalaFuture(url: String): scala.concurrent.Future[Response] = {
    val p = scala.concurrent.Promise[Response]

    val response = client.prepareGet(url).setFollowRedirect(true).execute()
    response.addListener(() => p.complete(Try(response.get())), null)

    p.future
  }
}
