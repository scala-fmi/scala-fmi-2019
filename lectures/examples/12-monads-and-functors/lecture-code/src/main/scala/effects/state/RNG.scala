package effects.state

import effects.Monad.ops._

case class RNG(seed: Long) {
  def nextInt: (RNG, Int) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = RNG(newSeed)
    val n = (newSeed >>> 16).toInt

    (nextRNG, n)
  }
}

object RNG {
  val nextInt = State((rng: RNG) => rng.nextInt)
  val nextBoolean = nextInt.map(_ > 0)
}

object RNGDemo extends App {
  import RNG._

  val randomTuple = for {
    a <- nextInt
    b <- nextInt
    c <- nextBoolean
  } yield (a, b, a + b, c)

  println(randomTuple.run(RNG(System.currentTimeMillis)))


  // Without State we have to do this:
  val (rng1, next1) = RNG(System.currentTimeMillis).nextInt
  val (rng2, next2) = rng1.nextInt
  val (rng3, next3) = rng2.nextInt

  println(next1, next2, next3)
}
