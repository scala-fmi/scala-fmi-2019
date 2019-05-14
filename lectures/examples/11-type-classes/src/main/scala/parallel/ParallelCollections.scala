package parallel

import math.impl.Monoid
import math.impl.Monoid.ops._
import parallel.Utils.time

import scala.collection.GenSeq

object ParallelCollections extends App {
  def sum[A : Monoid](xs: GenSeq[A]): A = {
    xs.fold(Monoid[A].identity)(_ |+| _)
//    xs.foldLeft(Monoid[A].identity)(_ |+| _)
  }

  val seq = 1 to 900000000

  println(time("Single threaded")(sum(seq)))
  println(time("Multi threaded")(sum(seq.par)))
}

object Utils {
  def time[T](name: String)(operation: => T): T = {
    val startTime = System.currentTimeMillis()

    val result = operation

    println(s"$name took ${System.currentTimeMillis - startTime} millis")

    result
  }
}
