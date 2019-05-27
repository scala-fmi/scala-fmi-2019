---
title: Implicits in Scala
---
#

> If there is one feature that makes Scala "Scala", I would pick implicits.
-- Martin Odersky

# Implicits in Scala

- Language feature
- Allow omitting method calls or variable references
- Compilation safety

# Implicits in Scala

- Values labeled with an `implicit` modifier can be passed to implicit parameters and used as implicit conversions
- `implicit` is an illegal modifier for top-level objects

# The compiler does not try to apply implicits if the code typechecks!

# Implicit conversions

- Historically came first in the language
- Allow arbitrary classes to implement new interfaces
- Prefer avoiding implicit conversions unless you have a very good reason to use them
- Scala 3 will restrict usage of implicit conversions

# Implicit conversions

An implicit conversion from type `A` to type `B` is defined by an implicit value that has a type signature `S => T` or `(=> S) => T`

# Implicit conversions

```scala
scala> val number: Double = 1
number: Double = 1.0
```

- scala.Predef

# Implicit conversions

```scala
scala> val number: Int = 1.23
<console>:11: error: type mismatch;
 found   : Double(1.23)
 required: Int
       val number: Int = 1.23
                         ^
```

# Implicit conversions

```scala
scala> implicit def doubleToInt(x: Double): Int = x.toInt
doubleToInt: (x: Double)Int
```

# Implicit conversions

```scala
scala> val number: Int = 1.23
number: Int = 1
```

# Implicit conversions

```scala
scala> val number: Int = doubleToInt(1.23)
number: Int = 1
```

# Rules

# Marking

- Definitions must be explicitly marked implicit to become available
- Variables, functions and object definitions can be marked `implicit`

```scala
def doubleToInt(double: Double): Int = double.toInt

implicit def doubleToInt(double: Double): Int = double.toInt
```

# Scope

- An implicit conversion must be in scope as a single identifier
- Otherwise the compiler will not consider it
- Companion objects bring member implicits in scope

# Resolution order

- Current scope
- Imports
- Companion objects
- Parameter arguments
- Type parameters
- Outer objects of nested types
- Parent objects

# One at a time

- The compiler only considers a single implicit insertion at a time
- This is done for compilation performance considerations
- It is possible to circumvent this

# Resolving ambiguity

- If there are more than one matching implicits, the most specific one is chosen
- If there is no unique most specific candidate, a compile error is reported


# When does the compiler try to apply implicits?

:::incremental

- Implicit conversion to an expected type
- Implicit conversion of a method call receiver
- Implicit parameters

:::

# Implicit conversion to an expected type

<div class="fragment">

```scala
// ISO 3166-1 alpha-2
class CountryCode(val code: String)

implicit def stringToCountryCode(str: String): CountryCode = new CountryCode(str)

def currencyFor(country: CountryCode): Currency = ???

currencyFor(country = "BG")

// Expanded
currencyFor(country = CountryCode("BG"))
```

</div>

# Implicit conversion of a method call receiver

# Type interop

```scala
class Rational(val n: Int, val d: Int) {
    def +(other: Rational): Rational = ???
    def +(other: Int): Rational = ???
}

new Rational(1, 2) + new Rational(3, 4)

new Rational(1, 3) + 2

1 + new Rational(1, 2) // Type mismatch
```

# Type interop

```scala
implicit def intToRational(x: Int): Rational = new Rational(x, 1)

1 + new Rational(1, 2) // Compiles correctly
```

# Simulate syntax

```scala
package scala
  object Predef {
    class ArrowAssoc[A](x: A) {
      def -> [B](y: B): Tuple2[A, B] = Tuple2(x, y)
    }
    implicit def any2ArrowAssoc[A](x: A): ArrowAssoc[A] =
      new ArrowAssoc(x)
    ...
}

Map(1 -> "one", 2 -> "two", 3 -> "three")
```

# Implicit classes

- Syntactic sugar for defining a class together with an implicit conversion

```scala
implicit class A(x: Int)
```

becomes

```scala
class A(x: Int)
implicit def A(x: Int): A = new A(x)
```
# Implicit classes

```scala
// ISO 3166-1 alpha-2
implicit class CountryCode(val code: String)

def currencyFor(country: CountryCode): Currency = ???

currencyFor(country = "BG")
```

# Implicit classes - Rich wrapper pattern

```scala
implicit class RichIterator[A](val iterator: Iterator[A]) {
  def zipWithNext: Iterator[(A, A)] = ...
}
```

# Implicit value classes

```scala
implicit class RichInt(val i: Int) extends AnyVal {
  def squared: Int = i * i
}
```

# Implicit parameters

Applicability:
- Type classes
- Context and config
- Dependency injection
- Proving theorems
- ...

# Implicit parameters

```scala
implicit val currency: Currency = Currency.getInstance("BGN")

def formattedText(price: BigDecimal)(implicit currency: Currency): String = ???

val price: BigDecimal = ???

formattedText(price)

// Expanded
formattedText(price)(currency)
```

# Proving theorems

```scala
/* if */   xs: List[List[A]]
/* then */ xs.flatten: List[A]
```

```scala
class List[A] {
  def flatten[B](implicit evidence: A =:= List[B]): List[B]
}
```

# Providing context using implicits

# Don't use implicits for conveniently passing ordinary parameters around!

# Traditional ways to express context

# Imperative way

- Global/shared mutable variables
- Global/shared constants
- Mutability is dangerous, constants are rigid

# Functional way

- Just pass anything you need as a parameter
- Type safe
- Sometimes gets tedious
- Error prone

# Scala way

- Leave some parameters _implicit_
- We provide just a type
- Compiler provides the rest

# Scala way

```scala
implicit val executionContext: ExecutionContext = ???

def queue(task: Task)(implicit ec: ExecutionContext): Future[Result] = ???
```

```scala
class Viewers(val persons: Set[Person])

def getScore(paper: Paper)(implicit viewers: Viewers): Int = {
  if (hasConflict(viewers, paper.authors)) -1
  else realScore(paper)
}

def getRanking(implicit viewers: Viewers): List[Paper] = {
  papers.sortBy(_.score)
}
```

# Cool stuff

- `:implicits`
- `-Xprint:typer`
- `@implicitNotFound`

# `Ordering` example

# Supplying implicit values

```scala
def implicitly[T]: T
```

# Supplying implicit values

```scala
def implicitly[T](implicit t: T): T = t
```
