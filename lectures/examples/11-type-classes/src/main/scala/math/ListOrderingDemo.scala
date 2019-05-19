package math

object ListOrderingDemo extends App {
  implicit def listOrdering[A : Ordering]: Ordering[List[A]] = new Ordering[List[A]] {
    def compare(x: List[A], y: List[A]): Int = {
      val aOrdering = Ordering[A]

      if (x == y) 0
      else if (x.isEmpty) -1
      else if (y.isEmpty) 1
      else if (x.isEmpty || aOrdering.lt(x.head, y.head)) -1
      else if (y.isEmpty || aOrdering.gt(x.head, y.head)) -1
      else compare(x.tail, y.tail)
    }
  }

  val sortedList = List(
    List(1, 2, 3),
    List(3, 4),
    List.empty[Int],
    List(1, 1)
  ).sorted

  println(sortedList)
}
