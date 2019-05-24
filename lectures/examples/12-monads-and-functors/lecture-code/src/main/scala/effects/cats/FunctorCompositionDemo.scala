package effects.cats

import cats.Functor
import cats.data.Nested
import cats.implicits._

object FunctorCompositionDemo extends App {
  val listOfOptions = List(Some(1), None, Some(2))

  println {
    // composition
    Functor[List].compose[Option].map(listOfOptions)(_ + 1)
  }

//  // Cats utilities:
//  println {
//    Nested(listOfOptions).map(_ + 1)
//  }
}
