package user

case class UsersRegistry(users: Map[String, User] = Map.empty) {
  def userFor(email: String): Option[User] = users.get(email)
  def contains(email: String): Boolean = users.contains(email)

  def add(user: User):UsersRegistry = UsersRegistry(users + (user.email -> user))
  def remove(email: String): UsersRegistry = UsersRegistry(users - email)
}
