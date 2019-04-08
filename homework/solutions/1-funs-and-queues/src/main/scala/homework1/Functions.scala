package homework1

import scala.annotation.tailrec

object Functions {
  def fromDigits(digits: List[Int], radix: Int = 10): Int = {
    @tailrec
    def fromDigits(digits: List[Int], acc: Int): Int = {
      if (digits.isEmpty) acc
      else fromDigits(digits.tail, acc * radix + digits.head)
    }

    fromDigits(digits, 0)
  }

  def parseInteger(integer: String, radix: Int = 10): Int = {
    def toNumericValue(digit: Char) = {
      if ('0' <= digit && digit <= '9') digit - '0'
      else if ('A' <= digit && digit <= 'Z') digit - 'A' + 10
      else throw new IllegalArgumentException("A non-digit found")
    }

    if (integer.nonEmpty && integer.head == '-') -parseInteger(integer.tail, radix)
    else fromDigits(integer.map(toNumericValue).toList, radix)
  }

  def zipMap(a: List[Int], b: List[Int], f: (Int, Int) => Int): List[Int] = {
    def zipMap(a: List[Int], b: List[Int], acc: List[Int]): List[Int] = {
      if (a.isEmpty || b.isEmpty) acc.reverse
      else zipMap(a.tail, b.tail, f(a.head, b.head) :: acc)
    }

    zipMap(a, b, List.empty)
  }

  def countCoinChangeVariants(denominations: List[Int], change: Int): Int = {
    if (change == 0) 1
    else if (change < 0 || denominations.isEmpty) 0
    else countCoinChangeVariants(denominations, change - denominations.head) +
      countCoinChangeVariants(denominations.tail, change)
  }

  def bfsTraversal(start: Int, end: Int, neighbours: Int => List[Int]): Queue = {
    @tailrec
    def bfs(toVisit: Queue, visited: Set[Int], path: Queue): Queue = {
      if (toVisit.isEmpty) path
      else {
        val current = toVisit.peek

        if (current == end) path.push(end)
        else if (visited(current)) bfs(toVisit.pop, visited, path)
        else bfs(
          toVisit.pop.push(neighbours(current)),
          visited + current,
          path.push(current)
        )
      }
    }

    bfs(Queue(List(start)), Set.empty, Queue.empty)
  }
}
