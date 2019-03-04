Обектно ориентирано програмиране за отчаяни със Скала
==========================================================
.. class:: center

|
|
| ФМИ 2018/2019 - Лекция №3
|
|

Soooo.... last time?
----------------------------------------

.. image:: images/03-oop/last-time.webp
    :class: scale
    :width: 700
    :height: 480
    :align: center

Всичка са писали обектно...нали?
----------------------------------------

.. class:: incremental

* Java, C++
* Python/Ruby
* C#, Typescript ?
* Rest of the world

tl;dr
----------------------------------------

.. class:: incremental

* Ще говорим за типове
* За UAP
* За classes, companion objects, traits and case classes
* Скала интерналс ООП
* ООП + ФП = ???


Накратко за nominal vs structured typing
------------------------------------------

.. class:: incremental

* Nominal - Аз съм бозайник, защото са ми татуирали "бозайник" на кожата
* Structural - Аз съм бозайник, защото малкото ми може да суче
* A koe e Скала?

Основи на ООП
----------------------------------------

.. class:: incremental

* Основен елемент на ООП - class -  **но в Scala...**
* Constructors, fields, methods - **но в Scala**
* Access levels - public, protected, private - **но в Scala...**


ООП в Скала - Classes
------------------------------------------

::

   class Animal

   class Mammal(order: String) extends Animal {
     var milk: Boolean = true
     val theMilk: Boolean = true
     def zeMilk: Boolean = true
   }


ООП в Скала - Companion objects
------------------------------------------

::

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

ООП в Скала - Traits
------------------------------------------

::

   trait Edible {
     def isPoisonous: Boolean
     def isDelicious: Boolean

     def ICanHazEat(): Boolean = !isPoisonous && isDelicious
   }

   class Mammal(
     val order: String,
     val isDelicious: Boolean = true,
     val isPoisonous: Boolean = false,
   ) extends Animal with Edible {
     private def milk: Boolean = true
   }


ООП в Скала - Case classes
------------------------------------------

::

   sealed trait Animal {
     val order: String
   }

   case class Mammal(
     order: String = "rodentia",
     milk: Boolean = true,
   ) extends Animal


Scala ООП в практиката
------------------------------------------

.. image:: images/03-oop/construction.jpg
    :class: scale
    :width: 700
    :height: 480
    :align: center


Next time....
------------------------------------------

.. image:: images/03-oop/enlightenment.jpg
    :class: scale
    :width: 700
    :height: 480
    :align: center

References
------------------------------------------

* SICP - https://mitpress.mit.edu/sites/default/files/sicp/index.html
* Structural typing examples - https://blog.carbonfive.com/2012/09/23/structural-typing-compile-time-duck-typing/
* UAP - Just google it
