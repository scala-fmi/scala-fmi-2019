package homework2

sealed trait Chain[+A] {
  def head: A
  def tail: Option[Chain[A]]

  def isEmpty: Boolean = ???

  def +:[B >: A](front: B): Chain[B] = ???

  def :+[B >: A](back: B): Chain[B] = ???

  def ++[B >: A](right: Chain[B]): Chain[B] = ???

  def foldLeft[B](initial: B)(f: (B, A) => B): B = ???

  def reduceLeft[B >: A](f: (B, A) => B): B = this.listify match {
    case Singleton(first) => first
    case Append(Singleton(first), rest) => rest.foldLeft(first: B)(f)
    case _ => sys.error("Unexpected listify format")
  }

  def map[B](f: A => B): Chain[B] = ???

  def flatMap[B](f: A => Chain[B]): Chain[B] = ???

  def foreach(f: A => Unit): Unit = foldLeft(())((_, next) => f(next))

  override def equals(that: Any): Boolean = that match {
    case c: Chain[_] => ???
    case _ => false
  }

  override def hashCode: Int = foldLeft(0) {
    _ * 31 + _.hashCode
  }

  override def toString: String = toList.mkString("Chain(", ",", ")")

  def toList: List[A] = foldLeft(List.empty[A])((acc, next) => next :: acc).reverse
  def toSet[B >: A]: Set[B] = foldLeft(Set.empty[B])((acc, next) => acc + next)

  def min[B >: A](implicit order: Ordering[B]): B = ???
  def max[B >: A](implicit order: Ordering[B]): B = ???

  def listify: Chain[A] = ???
}

case class Singleton[+A](head: A) extends Chain[A] {
  def tail: Option[Chain[A]] = None
}
case class Append[+A](left: Chain[A], right: Chain[A]) extends Chain[A] {
  def head: A = left.head
  def tail: Option[Chain[A]] = left match {
    case Singleton(_) => Some(right)
    case _ => listify.tail
  }
}

object Chain {
  def apply[A](head: A, rest: A*): Chain[A] = ???

  // Allows Chain to be used in pattern matching
  def unapplySeq[A](chain: Chain[A]): Option[Seq[A]] = Some(chain.toList)
}
