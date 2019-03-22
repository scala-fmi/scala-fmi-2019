package traits

class Animal

trait Edible {
  def isPoisonous: Boolean
  def isDelicious: Boolean

  def ICanHazEat(): Boolean = {
    !isPoisonous && isDelicious
  }
}

class Mammal(val order: String, val isDelicious: Boolean = true, val isPoisonous: Boolean = false) extends Animal with Edible {
  private def milk: Boolean = true
}

object Mammal {
  private[traits] val RODENT = "rodentia"
  private[traits] val PRIMATE = "primates"

  def apply(): Mammal = new Mammal(RODENT)
  def apply(order: String): Mammal = new Mammal(order)
  def apply(order: String, isDelicious: Boolean = true, isPoisonous: Boolean = false): Mammal = new Mammal(order, isDelicious, isPoisonous)

  def gotMilk(m:Mammal): Boolean = m.milk
}

object GotMilk {
  def main(args: Array[String]):Unit = {
    val mickeyMouse = Mammal()
    val amyTheGorilla = Mammal(order = Mammal.PRIMATE, isPoisonous = true)

    println(amyTheGorilla.order)
    println(s"My order is ${mickeyMouse.order} and I am ${if (mickeyMouse.ICanHazEat) "delicious" else "disgusting"}")
    println(s"My order is ${amyTheGorilla.order} and I am ${if (amyTheGorilla.ICanHazEat) "delicious" else "disgusting"}")
  }
}
