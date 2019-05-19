package types

import math.Rational

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object ClassMetainfoContextDemo extends App {
  // Does not compile because creation of Java arrays requires metainformation
  // (they are different for primitives and the different kind of objects)
  // def arrayOf[A](seq: A*) = Array[A](seq: _*)

  // ClassTag gives us the information we need for creating arrays
  def arrayOf[A : ClassTag](seq: A*) = Array[A](seq: _*)

  arrayOf(Rational(1), Rational(2))

  // On JVM no generic type info is available at runtime.
  // Scala allows accessing such info through a couple of metainfo type classes like TypeTag,
  // which are automatically created for every concrete type
  def listElementType[A : TypeTag](xs: List[A]) = {
    val t = typeOf[A]

    if (t =:= typeOf[Int]) "List of ints"
    else if(t =:= typeOf[Rational]) "List of rationals"
    else if (t =:= typeOf[List[String]]) "List of list of strings"
    else "List of something else"
  }

  val results = List(
    listElementType(List(Rational(2), Rational(3))), // List of rationals
    listElementType(List(List("a", "b"))), // List of rationals
    listElementType(List(List(1, 2))) // List of rationals
  )

  println(results)
}
