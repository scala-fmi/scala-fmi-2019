package authentication

import play.api.mvc._
import user.RegisteredUsersRepository

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedAction(registeredUsersRepository: RegisteredUsersRepository,
                          val parser: BodyParsers.Default)
                         (implicit val executionContext: ExecutionContext)
  extends ActionBuilder[AuthenticatedRequest, AnyContent] with ActionRefiner[Request, AuthenticatedRequest] {

  protected def refine[A](request:  Request[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {
    request.session.get("user") match {
      case Some(email) =>
        registeredUsersRepository.retrieveUser(email).map {
          case Some(user) => Right(new AuthenticatedRequest[A](user, request))
          case None => Left(Results.Unauthorized)
        }
      case None =>
        Future.successful(Left(Results.Unauthorized))
    }
  }
}
