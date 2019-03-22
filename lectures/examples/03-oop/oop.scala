package oop

class Animal

class Mammal(val order: String) extends Animal {
  var milk: Boolean = true
  val theMilk: Boolean = true
  def zeMilk: Boolean = true
}

object GotMilk {
  def main(args: Array[String]):Unit = {
    val mickeyMouse = new Mammal("rodentia")
    val amyTheGorilla = new Mammal("primates")

    println(s"My order is ${mickeyMouse.order} and I have all the milk ${mickeyMouse.milk} ${mickeyMouse.theMilk} ${mickeyMouse.zeMilk}")
    println(s"My order is ${amyTheGorilla.order} and I have all the milk ${amyTheGorilla.milk} ${amyTheGorilla.theMilk} ${amyTheGorilla.zeMilk}")
  }
}
