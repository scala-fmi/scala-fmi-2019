package user

import scala.concurrent.{ExecutionContext, Future}

class RegisteredUsersRepository(implicit ex: ExecutionContext) {
  @volatile private var usersRegistry: UsersRegistry = UsersRegistry()

  def retrieveUser(email: String): Future[Option[User]] = Future { // We use Future just for demonstration
    usersRegistry.userFor(email)
  }

  def registerUser(user: User): Future[User] = this.synchronized {
    if (usersRegistry.contains(user.email)) Future.failed(UserAlreadyExists(user.email))
    else {
      usersRegistry = usersRegistry.add(user)
      Future.successful(user)
    }
  }

  def deleteUser(email: String): Future[Unit] = Future { // We use Future just for demonstration
    this.synchronized {
      usersRegistry = usersRegistry.remove(email)
    }
  }
}

trait UsersRepositoryException extends Exception
case class UserAlreadyExists(email: String) extends UsersRepositoryException
