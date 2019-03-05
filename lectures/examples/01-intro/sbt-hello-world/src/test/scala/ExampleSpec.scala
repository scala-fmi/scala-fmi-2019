import org.scalatest._

class ExampleSpec extends FlatSpec with Matchers {
  "+" should "sum two numbers" in {
    2 + 3 should be (5)
  }

  "Lists sum" should "correctly sum nonempty List" in {
    val nonEmptyList = List(1, 2, 3)

    Lists.sum(nonEmptyList) should be (6)
  }

  "Lists sum" should "return zero for empty List" in {
    val emptyList = List.empty[Int]

    Lists.sum(emptyList) should be (0)
  }
}
