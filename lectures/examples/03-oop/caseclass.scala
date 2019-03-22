package caseclass

trait Animal {
  val order: String
}

case class Mammal(order: String = "rodentia", milk: Boolean = true) extends Animal

object GotMilk {
  def main(args: Array[String]):Unit = {
    val mickeyMouse = Mammal()
    val anyMouse = Mammal()

    val amy = mickeyMouse.copy(order = "primates")

    println(s"I am mickey and I am ${if (mickeyMouse == anyMouse) "ordinary" else "special"}!")
    println(s"And I am amy and I am a ${amy.order}")
  }
}
