package controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

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

class UsersController (cc: ControllerComponents) extends AbstractController(cc) {
  val getUser = Action { request =>
    val user = User("emo@gmail.com", 22, "Emil Dudev")

    // converts user to Json using the Writes typeclass implementation from User.userFormat (or User.userWrites)
    Ok(Json.toJson(user))
  }

  val postUser = Action(parse.json[User]) { request =>
    println(request.body)

    Ok
  }
}
