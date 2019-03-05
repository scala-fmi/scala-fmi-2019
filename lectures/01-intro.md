---
title: Функционално програмиране за напреднали със Scala
---
# Давайте функционалнооооооо\.....

![](images/01-intro/functional.jpg)

# Кратка история на ФП

::: incremental

-   Началото
-   Развитие на теория
-   Практическо навлизане

:::

# Scala навсякъде

::: incremental

-  Къде се ползва Scala? - Netflix, Amazon, Spotify, Twitter, Airbnb, etc
-  Scala с отворен код? - Kafka, Samza, Spark, Akka, Play Framework
-  Scala в България! - Ocado, Hopper & others

:::

# Кои сме ние?

# Административни неща

# Сбирки

Всяка сряда от 18:00 до 21:00 в зала 101

# Оценяване

::: incremental

- два теста (около средата и в края на семестъра) -- всеки по 25 точки
- проект -- 50 точки
- (до) 4 задачки през семестъра -- по 5 точки всяка

:::

# Оценяване - Скала (no-pun intended)

::: { .center-cells }

+--------+---------+
| Оценка | Точки   |
+--------+---------+
| 6      | ≥ 85    |
+--------+---------+
| 5      | 70--85  |
+--------+---------+
| 4      | 55--70  |
+--------+---------+
| 3      | 40-55   |
+--------+---------+
| 2      | \< 40   |
+--------+---------+

:::

# SCAlable LAnguage

- скалира с нуждата на потребителите на езика
- opinionated по подразбиране,
- но лесно позволява алтернативни конструкции/подходи според нуждата

# Симбиоза на ФП и ООП

# Детайлна типова система

![](images/01-intro/types.jpeg){ height="520" }

# Декларативност и композиция

```scala
// сумата на 100-те най-добри резултата на пълнолетните състезатели
competitors
  .filter(_.age >= 18)
  .map(_.score)
  .sorted(Ordering[Int].reverse)
  .take(100)
  .sum
```

# Подходящ за DSL-и

```scala
def square(x: Int) = x * x
def double(x: Int) = x * 2

square(double(square(10) * 100)) + 1
```

# Подходящ за DSL-и

```scala
// "магия"
implicit class Pipe[T](x: T) {
  def |>[V](f: T => V) = f(x)
}

10 |> square |> { double(_) * 100 } |> square |> { _ + 1 }
```

# Нови конструкции

```scala
def докато(cond: => Boolean)(body: => Any): Unit = {
  if (cond) {
    body
    докато(cond)(body)
  }
}

var i = 0
докато (i < 10) {
  println(i)
  i += 1
}
```

# Идеоматичен вариант в Scala

```scala
(1 until 10).foreach(println)
(1 until 10).sum
```

# Нови конструкции

асинхронни изчисления:

```scala
val calculation = Future {
  (1 until 10).sum
}
calculation.foreach(result => println("Result: " + result))
println("End of this thread")
```

Изход:

```scala
End of this thread
Result: 45
```

# Силна академична база

- но създаден за индустрията
- Композиращи се езикови елементи и малка граматика

# Grammar Size

![](images/01-intro/grammar-size.png){ height="520" }

# Силна академична база

- но създаден за индустрията
- Композиращи се езикови елементи и малка граматика
- Математическа база за Scala 3

# Scala 3 и Dotty

- базиран върху DOT -- математически модел за есенцията на Scala
- Бърз и опростен компилатор, с доста гъвкавост за развитие на езика и неговите инструменти

# Екосистема и стабилна общност

# Инсталиране

- JVM 8 или 11
  - [Win/OS X](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
  - Ubuntu/Debian: `install openjdk-8-jdk`
  - Fedora/Red Hat: `install java-1.8.0-openjdk`
- Scala 2.12
  - [Win, Linux](https://www.scala-lang.org/download/)
  - OS X: `brew install scala`
- sbt
  - [Win](https://www.scala-sbt.org/download.html)
  - OS X: `brew install sbt`
  - [Linux](https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html)

# Read-eval-print loop (REPL)

- интерактивен езиков шел
- стартира се от командния ред със `scala`

# Hello World

```scala
object HelloWorld {
  def main(args: Array[String]): Unit = {
    println("Hello, World!")
  }
}
```

# Компилиране и изпълнение

```
$ scalac HelloWorld.scala
$ scala HelloWorld
Hello, World!
```

# sbt, Scala/Simple Build Tool

build.sbt:

```scala
name := "hello-world"
version := "0.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
```

# sbt -- Директорийна структура

- `build.sbt`
- `src/main/scala` -- основен код
- `src/test/scala` - тестове

# sbt команди

- sbt \<команда\> -- изпълнява командата
- sbt -- влиза в интерактивен режим
- compile -- компилира кода
- run -- изпълнява обект с `main` метод
- console -- стартира REPL, в който е достъпно всичко от кода

# IDEs/текстови редактори

- IntelliJ IDEA ([Community Edition](https://www.jetbrains.com/idea/download)) – ще ползваме основно него
- Scala IDE for Eclipse
- Ensime – IDE възможности за vim, Emacs, Sublime, Atom, VSC

# Демо с IntelliJ IDEA

# Тестове


```scala
import org.scalatest._

class ExampleSpec extends FlatSpec with Matchers {
  "+" should "sum two numbers" in {
    2 + 3 should be (5)
  }
}
```

# Ресурси -- книги

![](images/01-intro/programming-in-scala.png){ height="320" }
![](images/01-intro/functional-programming-in-scala.jpg){ height="320" }
![](images/01-intro/essential-scala.png){ height="320" }

# Ресурси

- [Документация](https://docs.scala-lang.org/)
- [Scala API](https://docs.scala-lang.org/api/all.html)
- [Scala Style Guide](https://docs.scala-lang.org/style/)
