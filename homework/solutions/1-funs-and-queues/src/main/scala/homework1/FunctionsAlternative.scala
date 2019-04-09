package homework1

import scala.annotation.tailrec

object FunctionsAlternative {
  def fromDigits(digits: List[Int], radix: Int = 10): Int = digits.foldLeft(0)((acc, digit) => acc * radix + digit)

  def parseInteger(integer: String, radix: Int = 10): Int = {
    if (integer.nonEmpty && integer.head == '-') -parseInteger(integer.tail, radix)
    else fromDigits(integer.map(_.asDigit).toList, radix)
  }

  def zipMap(a: List[Int], b: List[Int], f: (Int, Int) => Int): List[Int] = a.zip(b).map(f.tupled)

  def countCoinChangeVariants(denominations: List[Int], change: Int): Int = {
    if (change == 0) 1
    else if (change < 0 || denominations.isEmpty) 0
    else countCoinChangeVariants(denominations, change - denominations.head) +
      countCoinChangeVariants(denominations.tail, change)
  }

  def bfsTraversal(start: Int, end: Int, neighbours: Int => List[Int]): Queue = {
    @tailrec
    def bfs(toVisit: Queue, reached: Set[Int], path: Queue): Queue = {
      if (toVisit.isEmpty) path
      else if (toVisit.peek == end) path.push(end)
      else {
        val current = toVisit.peek
        val newNeighbours = neighbours(current).filter(!reached(_))

        bfs(
          toVisit.pop.push(newNeighbours),
          reached ++ newNeighbours,
          path.push(current)
        )
      }
    }

    bfs(Queue(List(start)), Set(start), Queue.empty)
  }
}
