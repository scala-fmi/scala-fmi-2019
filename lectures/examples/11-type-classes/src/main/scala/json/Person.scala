package json

case class Person(name: String, email: String, age: Int)

object Person {
  implicit val personSerializable = new JsonSerializable[Person] {
    def toJson(person: Person): JsonValue = JsonObject(Map(
      "name" -> JsonString(person.name),
      "email" -> JsonString(person.email),
      "age" -> JsonNumber(person.age)
    ))
  }
}

