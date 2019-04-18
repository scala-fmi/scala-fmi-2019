
sealed trait FStream[+A] {

  def head():A ={
    this match {
      case FEmpty => ???
      case FCons(h, _) => h
    }
  }

  def tail(): FStream[A] = {
    this match {
      case FEmpty => ???
      case FCons(_, tl) => tl()
    }
  }

  def take(n: Int): FStream[A] = {
    this match {
      case FCons(h, tl) if (n > 0) => FCons(h, () => tl().take(n - 1))
      case _ => FEmpty
    }
  }

}

case object FEmpty extends FStream[Nothing]
case class FCons[+A](h: A, tl:() => FStream[A])extends FStream[A]


object FStream {

  def con[A](head: => A, tail: => FStream[A]) :FStream[A] = {
    val h = head
    lazy val tl = tail
    FCons(h, () => tl)
  }

  def apply[A](a:A*): FStream[A] = {
    if(a.isEmpty) FEmpty
    else con(a.head, apply(a.tail:_*))
  }

}

