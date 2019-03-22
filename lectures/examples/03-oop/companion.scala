package companion

class Animal

class Mammal(val order: String) extends Animal {
  private def milk: Boolean = true
}

object Mammal {
  private[companion] val RODENT = "rodentia"
  private[companion] val PRIMATE = "primates"

  def apply(): Mammal = new Mammal(RODENT)
  def apply(order: String): Mammal = new Mammal(order)

  def gotMilk(m:Mammal): Boolean = m.milk
}

object GotMilk {
  def main(args: Array[String]):Unit = {
    val mickeyMouse = Mammal()
    val amyTheGorilla = Mammal(Mammal.PRIMATE)

    println(s"My order is ${mickeyMouse.order} and I have all the milk: ${Mammal.gotMilk(mickeyMouse)}")
    println(s"My order is ${amyTheGorilla.order} and I have all the milk: ${Mammal.gotMilk(amyTheGorilla)}")
  }
}
