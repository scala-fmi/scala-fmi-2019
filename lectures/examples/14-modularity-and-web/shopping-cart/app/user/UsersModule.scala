package user

import scala.concurrent.ExecutionContext

trait UsersModule {
  def executionContext: ExecutionContext

  lazy val registeredUsersRepository = new RegisteredUsersRepository()(executionContext)
}
