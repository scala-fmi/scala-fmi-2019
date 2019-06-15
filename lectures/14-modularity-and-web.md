---
title: Модулност и Web програмиране със Scala
---
#

Функционалните програми се състоят в голяма степен от чисти функции без състояние

Как да ги организираме в нашия код?

#

Чистите функции често рефирират към други конкретни функции.

```scala
def function(xs: List[String], n: Int) = {
  xs.zipWithIndex.map {
    case (x, index) => ObjectA.anotherFunction(x, index % n)
  }
}
```

#

В някои случаи, особено при работа с конкретен домейн, е доста вероятно да искаме да се абстрахираме от конкретната имплементация на функциите, от които зависим

#

Във ФП това може да постигнем като функцията се подава като параметър:

```scala
def function(meaningfulTransformation: (String, Int) => String)
            (xs: List[String], n: Int) = {
  xs.zipWithIndex.map {
    case (x, index) => meaningfulTransformation(x, index % n)
  }
}

val functionWithObjectA = function(ObjectA.anotherFunction) _

functionWithObjectA(List("a", "bc", "def"), 11)

```

# Dependency injection

Този подход наричаме със сложното име "Dependency injection"

<p class="fragment">Представлява вид inversion of control – поради това, че функцията вече не създава/реферира изрично конкретна зависимост, ами я приема като параметър</p>

# Dependency injection с ООП модулност

:::incremental

* Този подход работи добре, но може да направи функциите доста сложни
* В Scala можем да използваме ООП класовете и trait-овете за да решим това

:::

# Dependency injection с ООП модулност

```scala
class SomeClass(meaningfulTransformation: (String, Int) => String) {
  def function(xs: List[String], n: Int) = {
    xs.zipWithIndex.map {
      case (x, index) => meaningfulTransformation(x, index % n)
    }
  }
  
  def anotherFunction = ???
}

val someObject = new SomeClass(ObjectA.anotherFunction)

someObject.function(List("a", "bc", "def"), 11)
```

# Dependency injection с ООП модулност

```scala
class SomeClass(meaningfulTransformation: MeaningfulTransformation) {
  def function(xs: List[String], n: Int) = {
    xs.zipWithIndex.map {
      case (x, index) => meaningfulTransformation(x, index % n)
    }
  }
  
  def anotherFunction = ???
}

val someObject = new SomeClass(ObjectA.anotherFunction)

someObject.function(List("a", "bc", "def"), 11)
```

```scala
trait MeaningfulTransformation {
  def apply(str: String, n: Int): String
}
```

# Защо dependency injection?

:::incremental

* less coupling, more flexibility
* не сме зависими от конкретна имплементация (която може да се промени)
* позволява тестване – подменяме зависимостите със специално подбрани инстанции
* става ясно от какво всъщност зависи определен компонент

:::

# Dependency injection – кой навързва зависимостите?

:::incremental

* Runtime – популярно в Java света ([Guice](https://github.com/google/guice), [Spring](https://spring.io/))<span class="fragment">. Не бива валидирано по време на компилация</span>
* Compile-time

:::

# Compile-time dependency injection – демо

# Thin cake pattern

# Runtime конфигурация

# Как да определим кои са ни модулите?

:::incremental

* инфраструктурни/библиотечни – занимаващи се с конкретна библиотека или конкретна част от инфраструктурата на приложението (конфигурация и помощни функции за базата, за HTTP комуникация и т.н.)
* домейн модули – за всеки домейн/поддомейн

:::

# Как да определим кои са ни модулите?

::: incremental

* Накрая ще разгледаме приложение за уеб магазин
* Неговият домейн може да бъде разделен на следните поддомейни, всеки от тях различен модул:
  - Управление на потребителите (регистрация, информация за потребителите, аутентикация)
  - Инвентар (продукти, наличност и т.н.)
  - Магазин и поръчки
* Това разделение е естествено и обикновено позволява една промяна да засегне само един модул. Също така позволява по-лесно евентулно отделяне на модулите

:::

# Как да определим кои са ни модулите?

В някои проекти в индустрията ще срещнете разделение по слоеве – model, repository, service, controller, и т.н. Това почти винаги не работи добре, тъй като една промяна започва да засяга много пакети/модули. Също така компонентите, които имат строга връзка помежду си, остават разделечени и губят своята локалност, което прави кодът по-труден за проследяване.

Разделянето по слоеве не трябва да е основно

# Повече информация

[DI in Scala guide](https://di-in-scala.github.io/)

# Изразителност на функционалното програмиране

:::incremental

* Основна полза на функционалното програмиране е възможността за абстрактност
* Но най-силно изразителността му се проличава когато добавим конкретика и опитаме да моделираме конкретен домейн
* Често създаваме конкретни функционални DSL/библиотека за определен домейн
* Но много техни аспекти моделираме срещу познати ни абстракции, като моноид, монада, апликатив и т.н.

:::

# Домейн: Web и HTTP

# HTTP библиотеки за Scala (с допълнения)

:::incremental

* [cats](https://typelevel.org/cats/) + [http4s](https://http4s.org/) + [circe](https://circe.github.io/circe/) + [doobie](https://tpolecat.github.io/doobie/)
* [Akka HTTP](https://doc.akka.io/docs/akka-http/current/index.html)
* [Play Framework](https://www.playframework.com/) (a bit opinionated)

:::

# Play Framework

# Play Framework – добавяне към проекта

* променя стандартните настройки на sbt
  - `src/main/scala` отива в `app`
  - `src/test/scala` в `test`
  - `src/main/resources` в `conf`
* `project/plugins.sbt`:
  
  ```scala
  addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.2")
  ```
* `build.sbt`:
  
  ```scala
    name := "web-app"
    version := "0.1"
    
    lazy val root = (project in file(".")).enablePlugins(PlayScala)
    
    libraryDependencies ++= Seq(
      ws, // Web client library, coming from the Play Framework
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test
    )
  ```

# Play Framework – стартиране

:::incremental

* `sbt run` – стартира се в development режим на [http://localhost:9000](http://localhost:9000). При отваряне, ако има нужда, приложението автоматично се рекомпилира и рестартира
* `sbt runProd` – стартиране в production режим
* `sbt stage` – пакетиране на приложението, така че да може да бъде стартирано самостоятелно 

:::

# HTTP app

![](images/14-modularity-and-web/04fig02_alt.jpg)

# Action

Обработва request до HTTP response

# Action

```scala
trait EssentialAction extends RequestHeader => Accumulator[ByteString, Result]
```

:::incremental

* Това е най-общият и най-сложният вид

:::

# Action

Play Framework предоставя по-лесен за употреба DSL за често срещаните обработки

::: incremental

* синхронно генериране на резултат от request:
  
  `Action(Request[AnyContent] => Response)`
* асинхронно генериране на резултат от request:
  
  `Action.async(Request[AnyContent] => Future[Response])`
* парсване на тялото на request-а:
  
  `Action(parse.text)(Request[String] => Response`)
* комбинация:
  
  `Action.async(parse.text)(Request[String] => Future[Response])`

:::

# HTTP app

![](images/14-modularity-and-web/04fig03_alt.jpg)

# Json

```scala
trait Writes[-A] {
  def writes(o: A): JsValue
}
```

<div class="fragment">

```scala
trait Reads[A] {
  def reads(json: JsValue): JsResult[A]
}
```

</div>

<div class="fragment">

```scala
sealed trait JsResult[+A]
case class JsSuccess[T](value: T, path: JsPath = JsPath()) extends JsResult[T]
case class JsError(errors: collection.Seq[(JsPath, collection.Seq[JsonValidationError])]) extends JsResult[Nothing]
```

</div>

<div class="fragment">

```scala
trait Format[A] extends Writes[A] with Reads[A]
```

</div>

# Body parsing

# Shopping App
