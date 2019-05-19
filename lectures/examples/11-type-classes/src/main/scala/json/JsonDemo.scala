package json

import json.JsonSerializable.jsonObj
import json.JsonSerializable.ops._

object JsonDemo extends App {
  List(1, 2, 3).toJsonString // [1, 2, 3]

  val ivan = Person("Ivan", "ivan@abv.bg", 23)
  val georgi = Person("Georgi", "georgi@gmail.bg", 28)

  ivan.toJson // JsonObject(Map(name -> JsonString(Ivan), email -> JsonString(ivan@abv.bg), age -> JsonNumber(23)))
  ivan.toJsonString // {"name": "Ivan", "email": "ivan@abv.bg", "age": 23}

  // Person's JsonSerializable composes with List's JsonSerializable
  List(ivan, georgi).toJsonString // [{"name": "Ivan", "email": "ivan@abv.bg", "age": 23}, {"name": "Georgi", "email": "georgi@abv.bg", "age": 28}]

  {
    // We might want to skip some fields, like email, for example because we don't want to share
    // user's email with other users.
    // It's really easy to do so with this implementation -
    // just provide another Person's JsonSerializable in this context

    implicit val personSerializable = new JsonSerializable[Person] {
      // Using the jsonObj utility
      def toJson(person: Person): JsonValue = jsonObj(
        "name" -> person.name,
        "age" -> person.age
      )
    }

    // It will compose just as easily
    println {
      List(ivan, georgi).toJsonString // [{"name": "Ivan", "age": 23}, {"name": "Georgi", "age": 28}]
    }
  }

  // We can implement this for deserialization, too. play-json is an example for library that implements this pattern
  // and provides many utilities to ease the creation of (de)serializers.
  // Since the mapping depends on only compile-time information the implementation is faster than e.g. Jackson (which uses reflection)
}
