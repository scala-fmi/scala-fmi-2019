package effects.lecture.examples

case class RNG(seed: Long) {
  def nextInt: (RNG, Int) = {
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRNG = RNG(newSeed)
    val n = (newSeed >>> 16).toInt

    (nextRNG, n)
  }
}

object RNG {
}

object RNGDemo extends App {
}
