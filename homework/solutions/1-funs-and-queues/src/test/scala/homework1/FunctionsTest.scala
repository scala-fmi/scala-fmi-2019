package homework1

import org.scalatest.{FlatSpec, Matchers}
import Functions._

class FunctionsTest extends FlatSpec with Matchers {
//  "fromDigits" should "return 0 for an empty list" in {
//    fromDigits(List.empty, 16) shouldBe 0
//  }

  it should "return the digit for a single digit list" in {
    fromDigits(List(12), 16) shouldBe 12
  }

  it should "form a decimal number" in {
    fromDigits(List(1, 2, 3)) shouldBe 123
  }

  it should "form a binary number" in {
    fromDigits(List(0, 1, 1, 0, 1, 0, 0), 2) shouldBe 52
  }

  it should "form a hex number" in {
    fromDigits(List(1, 12, 4), 16) shouldBe 452
  }

  "parseInteger" should "parse a decimal number" in {
    parseInteger("123") shouldBe 123
  }

  it should "parse a binary number" in {
    parseInteger("0110100", 2) shouldBe 52
  }

  it should "parse a hex number" in {
    parseInteger("1C4", 16) shouldBe 452
  }

  it should "parse a 36-base number" in {
    parseInteger("1F5Z0", 36) shouldBe 2387196
  }

  it should "parse a negative number" in {
    parseInteger("-1C4", 16) shouldBe -452
  }

  it should "parse a zero" in {
    parseInteger("0") shouldBe 0
  }

  "zipMap" should "transform two lists" in {
    zipMap(List(1, 2, 3), List(4, 5, 6), _ * _) shouldBe List(4, 10, 18)
  }

  it should "limit the output length to the shorter list" in {
    zipMap(List(3, 6), List(20, 30, 40), (x, y) => y - x) shouldBe List(17, 24)
    zipMap(List(3, 6, 9), List(20, 30), (x, y) => y - x) shouldBe List(17, 24)
  }

  it should "produce an empty list if a or b is empty" in {
    zipMap(List.empty, List(1, 2, 3), _ * _) shouldBe List.empty
    zipMap(List(1, 2, 3), List.empty, _ * _) shouldBe List.empty
    zipMap(List.empty, List.empty, _ * _) shouldBe List.empty
  }

  "countCoinChangeVariants" should "count the ways to give a change" in {
    countCoinChangeVariants(List(1, 2, 5), 6) shouldBe 5
    countCoinChangeVariants(List(3, 8, 15, 20, 50), 495) shouldBe 10005
  }

  it should "produce 1 if the change is 0" in {
    countCoinChangeVariants(List(1, 2, 5), 0) shouldBe 1
    countCoinChangeVariants(List.empty, 0) shouldBe 1
  }

  it should "produce 0 if the change is not 0 and the denominations list is empty" in {
    countCoinChangeVariants(List.empty, 5) shouldBe 0
  }

  it should "produce 0 if the change cannot be achieved with the available denominations" in {
    countCoinChangeVariants(List(2, 4, 6, 10, 14), 11) shouldBe 0
  }

  it should "produce correct result no matter the order of denominations" in {
    countCoinChangeVariants(List(1, 2, 5), 1000) shouldBe 50401
    countCoinChangeVariants(List(1, 5, 2), 1000) shouldBe 50401
    countCoinChangeVariants(List(2, 1, 5), 1000) shouldBe 50401
    countCoinChangeVariants(List(2, 5, 1), 1000) shouldBe 50401
    countCoinChangeVariants(List(5, 1, 2), 1000) shouldBe 50401
    countCoinChangeVariants(List(5, 2, 1), 1000) shouldBe 50401
  }

  type Graph = Map[Int, List[Int]]

  val fullGraph: Graph = Map(
    1 -> List(2, 5, 8),
    2 -> List(1, 3, 6),
    3 -> List(2, 4),
    4 -> List(3),
    5 -> List(6),
    6 -> List(7),
    8 -> List(9)
  ).withDefaultValue(List.empty)

  def toList(queue: Queue): List[Int] = {
    if (queue.isEmpty) List.empty
    else queue.peek :: toList(queue.pop)
  }

  "bfsTraversal" should "find path starting from root" in {
    val result = bfsTraversal(1, 6, fullGraph)

    toList(result) shouldBe List(1, 2, 5, 8, 3, 6)
  }

  it should "find path starting from bottom" in {
    val result = bfsTraversal(4, 6, fullGraph)

    toList(result) shouldBe List(4, 3, 2, 1, 6)
  }

  it should "not go through a node more than once" in {
    val graph = Map(
      1 -> List(2, 3),
      2 -> List(4),
      3 -> List(4),
      4 -> List(5)
    ).withDefaultValue(List.empty)

    val result = bfsTraversal(1, 5, graph)

    toList(result) shouldBe List(1, 2, 3, 4, 5)
  }

  it should "return the path so far even if the end has not been reached" in {
    val graph = fullGraph ++ Map(
      2 -> List(1, 3),
      5 -> List.empty
    )

    val result = bfsTraversal(1, 6, graph)

    toList(result) shouldBe List(1, 2, 5, 8, 3, 9, 4)
  }

  it should "return only the start if it doesn't have neighbours" in {
    val result = bfsTraversal(1, 10, (Map.empty: Graph).withDefaultValue(List.empty))

    toList(result) shouldBe List(1)
  }
}
