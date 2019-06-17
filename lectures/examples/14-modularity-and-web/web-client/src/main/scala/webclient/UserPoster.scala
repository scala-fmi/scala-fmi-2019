package webclient

import play.api.libs.json.{JsSuccess, JsValue, Json}
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}

// implicits that tell the client how to write JSON as an HTTP body
import play.api.libs.ws.JsonBodyWritables._
// Allows response.body[JsValue]
import play.api.libs.ws.JsonBodyReadables._

case class User(email: String, age: Int, name: String)
object User {
  //  // The machanism we know:
  //  implicit val userWrites = new Writes[User] {
  //    def writes(user: User): JsValue = Json.obj(
  //      "email" -> JsString(user.email),
  //      "age" -> JsNumber(user.age),
  //      "name" -> JsString(user.name)
  //    )
  //  }

  // Uses a compile time macro instead. Autogenerates code similar to the above, for both writes and reads
  implicit val userFormat = Json.format[User]
}

class UserPoster(wsClient: StandaloneAhcWSClient)
                (implicit ec: ExecutionContext) {
  def post(email: String, age: Int, name: String): Future[User] = {
    val user = User(email, age, name)

    wsClient.url("https://postman-echo.com/post") // postman-echo is just a testing server, that generates json
      .post(Json.toJson(user)) // Serialize user to JSON using User.userFormat
      .map(_.body[JsValue] \ "data") // postman-echo returns a JSON with the original user in a "data" field
      .map(_.validate[User]) // Deserialize JSON to User using User.userFormat. Can be either JsSuccess or JsError
      .collect {
        case JsSuccess(receivedUser, _) => receivedUser
        case _ => throw new RuntimeException("unexpected")
      }
  }
}
