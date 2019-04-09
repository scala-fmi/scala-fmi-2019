package homework1

import org.scalatest.{FlatSpec, Matchers}

class QueueTest extends FlatSpec with Matchers {
  def toList(queue: Queue): List[Int] = {
    if (queue.isEmpty) List.empty
    else queue.peek :: toList(queue.pop)
  }

  "an empty queue" should "produce a queue with a single element when that element is added to it" in {
    val emptyQueue = Queue.empty
    val singleElementQueue = emptyQueue.push(42)

    singleElementQueue.peek shouldBe 42
    singleElementQueue.size shouldBe 1
  }

  it should "throw a NoSuchElementException when peeked" in {
    a [NoSuchElementException] should be thrownBy Queue.empty.peek
  }

  it should "throw a NoSuchElementException when popped" in {
    a [NoSuchElementException] should be thrownBy Queue.empty.pop
  }

  it should "be empty" in {
    Queue.empty.isEmpty shouldBe true
  }

  it should "have a size of 0" in {
    Queue.empty.size shouldBe 0
  }

  it should "be immutable" in {
    val emptyQueue = Queue.empty

    emptyQueue.push(42)

    emptyQueue.isEmpty shouldBe true
  }

  "a queue" should "work with a mix of operations" in {
    val queue1 = Queue.empty.push(1).push(2).push(3)
    val queue2 = queue1.pop.pop
    val queue3 = queue2.push(4).push(5).push(6).pop.push(7)

    queue1.isEmpty shouldBe false
    queue1.size shouldBe 3
    queue1.peek shouldBe 1
    toList(queue1) shouldBe List(1, 2, 3)

    queue2.isEmpty shouldBe false
    queue2.size shouldBe 1
    queue2.peek shouldBe 3
    toList(queue2) shouldBe List(3)

    queue3.isEmpty shouldBe false
    queue3.size shouldBe 4
    queue3.peek shouldBe 4
    toList(queue3) shouldBe List(4, 5, 6, 7)
  }

  it should "be able to grow, shrink to empty, and then grow again" in {
    val initialQueue = Queue.empty.push(1).push(2).push(3).push(4)

    initialQueue.isEmpty shouldBe false
    initialQueue.size shouldBe 4

    val emptiedQueue = initialQueue.pop.pop.pop.pop

    emptiedQueue.isEmpty shouldBe true
    a [NoSuchElementException] should be thrownBy emptiedQueue.peek

    val regrownQeueue = emptiedQueue.push(5).push(6).push(7)

    regrownQeueue.isEmpty shouldBe false
    toList(regrownQeueue) shouldBe List(5, 6, 7)
  }

  "Queue.apply(xs)" should "create a queue from the sequence" in {
    toList(Queue(List(1, 2, 3, 4))) shouldBe List(1, 2, 3, 4)
    Queue(List.empty).isEmpty shouldBe true
  }
}
