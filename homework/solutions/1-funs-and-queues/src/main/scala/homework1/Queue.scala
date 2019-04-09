package homework1

class Queue private (front: List[Int], back: List[Int] = List.empty) extends Iterable[Int] {
  def peek: Int = {
    if (front.isEmpty) back.last
    else front.head
  }

  def push(n: Int): Queue = new Queue(front, n :: back)

  def push(xs: Seq[Int]): Queue = new Queue(front, xs.reverse.toList ::: back)

  def pop: Queue = {
    if (isEmpty) throw new NoSuchElementException
    else if (front.isEmpty) new Queue(back.reverse).pop
    else new Queue(front.tail, back)
  }

  override def isEmpty: Boolean = front.isEmpty && back.isEmpty
  override def size: Int = front.size + back.size

  def iterator: Iterator[Int] = front.iterator ++ back.reverseIterator
}

object Queue {
  def empty: Queue = new Queue(List.empty)

  def apply(xs: Seq[Int]): Queue = new Queue(xs.toList)
}
