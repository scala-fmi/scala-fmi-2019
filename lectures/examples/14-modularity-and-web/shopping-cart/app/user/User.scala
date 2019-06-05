package user

import play.api.libs.json.Json

case class User(email: String, name: String, age: Int, password: String)

object User {
  implicit val userFormat = Json.format[User]
}