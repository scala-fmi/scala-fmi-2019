object Lists {
  def sum(xs: List[Int]): Int = xs.foldLeft(0)(_ + _)
}
