package authentication

import play.api.mvc.{Request, WrappedRequest}
import user.User

class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)