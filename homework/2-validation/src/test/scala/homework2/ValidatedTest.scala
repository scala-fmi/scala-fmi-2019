package homework2

import org.scalatest.{FlatSpec, FunSuite, Matchers}

class ValidatedTest extends FlatSpec with Matchers {
  "zip" should "combine valid instances" in {
    Valid(1).zip(Valid("a")) shouldEqual Valid((1, "a"))
  }

  it should "combine errors from invalid instances" in {
    Invalid(1).zip(Invalid(Chain(2, 3))) shouldEqual Invalid(Chain(1, 2, 3))
  }
}
