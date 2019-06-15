package controllers

import authentication.AuthenticatedAction
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import user.{RegisteredUsersRepository, User, UserAlreadyExists, UsersRegistry}

import scala.concurrent.ExecutionContext

case class UserCredentials(email: String, password: String) {
}

object UserCredentials {
  implicit val userCredentialsRead = Json.reads[UserCredentials]
}

class UserController(cc: ControllerComponents,
                     registeredUsersRepository: RegisteredUsersRepository,
                     authenticatedAction: AuthenticatedAction)
                    (implicit ex: ExecutionContext) extends AbstractController(cc) {
  def register = Action.async(parse.json[User]) { request =>
    registeredUsersRepository.registerUser(request.body)
      .map { _ => Ok }
      .recover {
        case UserAlreadyExists(_) => Conflict
      }
  }

  def login = Action.async(parse.json[UserCredentials]) { request =>
    val credentials = request.body
    registeredUsersRepository.retrieveUser(credentials.email).map {
      case Some(user) if user.password == credentials.password =>
        Ok.withSession(
          "user" -> user.email
        )
      case _ => Unauthorized
    }
  }

  def user = authenticatedAction { request =>
    Ok(Json.toJson(request.user))
  }

  def logout = Action {
    Ok.withNewSession
  }
}
