---
title: Монади и функтори
---
# Предния път – Type classes

:::incremental

* моделират типове (а не просто обекти)
* аксиомите (type class laws) са важни
* позволяват ретроактивен ad hoc полиморфизъм
* в Scala са реализирани чрез implicits и съответно могат да са контекстно-зависими
:::

# Ефекти

* Option – частичност
* Try – успех/грешка с изключение
* Either – успех/грешки
* Validated – валидация с множествено грешки
* List – недетерминизъм, множественост
* IO – вход/изход
* Future – (eager) асинхронност
* Task – (lazy) асихронност
* Stream – lazy поток
* State – състояние
* Iteratee – консуматор на поток
* DBAction – заявка/действие върху базата

# Операции върху ефекти

```scala
val a = 42 // независими
val b = 4  // изчисления

val c = a + b // операция
val d = (a + b) * 10 // композиция на операции
val e = f(g(a)) // композиция на функции
```

Пренесохме възможността за тези операции върху ефекта `Future`<br/>
(и стойността в него)

* `map` – трансформация на единична стойност (напр. `val c = -a`)
* `map2`, или още `zipMap` или `zipWith` – трансформация на две независими стойности (`val c = a + b`). Резултатът `c` зависи от тях
* `map3`, `zipMap3`...; `mapN` дефинира зависимости
* `flatMap` – когато функциите в изразите са ефектни, напр. ако `f` и `g` връщат `Future`
* `flatMap` – ефектна трансформация на единична стойност

#

Нека да генерализираме тези операции в type class-ове

<p class="fragment">Ще започнем от една различна гледна точка</p>

# Композиция на функции

Нека имаме функции f: A => B и g: B => C

<p class="fragment">Тогава h(x) = g(f(x)) е функция от тип A => C</p>

<p class="fragment">h = g ∘ f</p>

# Композитност на функции – аксиоми

:::incremental

* асоциативност – нека f: A => B, g: B => C и h: C => D. Тогава:
  
  ```
  (h ∘ g) ∘ f = h ∘ (g ∘ f)
  ```
* неутрален елемент – нека identity = x => x. Тогава ∀ f
  
  ```
  identity ∘ f = f ∘ identity = f
  ``` 

:::

# Композитност на функции

h ∘ g ∘ f

# Ефектни функции

Функция, връщаща стойност, затворена в ефект

<div class="fragment">
```
A => Option[B]
A => Future[B]
A => Validated[E, B]
```
</div>

# Композитност на ефектни функции?

Нека<br />
f: A => Option[B],<br />
g: B => Option[C],<br />
h: C => Option[D]

<p class="fragment">h ∘ g ∘ f?</p>

<p class="fragment">За всеки ефект имплементацията е различна</p>

# Option, ако нямаме `flatMap`

```scala
def compose[A, B, C, D](f: A => Option[B],
                        g: B => Option[C],
                        h: C => Option[D]): A => Option[D] = a => {
  val fOption = f(a)
  if (fOption != None) {
    val gOption = g(fOption.get)
    if (gOption != None){
      h(gOption.get)
    } else {
      None
    }
  } else {
    None
  }
}
```

<p class="fragment">Прилича на callback hell код</p>

<p class="fragment">Често срещано при работа с `null` в Java</p>

# Type class за композиране

# Монада

```scala
trait Monad[F[_]] {
  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C]
  def unit[A](a: A): F[A]
}
```

<p class="fragment">Тук `F` е конструктор на тип, а не тип</p>

<p class="fragment">`F` е higher-kinded type (тип от по-висок ред)</p>

<p class="fragment">Пример: List е конструктор на тип, List[Int] е тип</p>

<p class="fragment">higher-kinded polymorphism</p>

# Монада – аксиоми

:::incremental

* асоциативност:
  
  ```
  compose(compose(f, g), h) == compose(f, compose(g, h))
  ```
* неутрален елемент 
  
  ```
  compose(unit, f) == compose(f, unit) == f
  ```

:::

# Монада за Option

```scala
val optionMonad = new Monad[Option] {
  def compose[A, B, C](f: A => Option[B], g: B => Option[C]) =
    (a: A) => f(a) match {
      case Some(b) => g(b)
      case _ => None
    }
  def unit[A](a: A): Option[A] = Some(a)
}
```

# Алтернативна дефиниция<br />чрез `flatMap`

<div class="fragment">

```scala
trait Monad[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def unit[A](a: => A): F[A]

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] =
    a => flatMap(f(a))(g)
}
```

</div>


<div class="fragment">

`flatMap` може да се изрази чрез compose като:

```scala
compose((_: Unit) => fa, f)(())
```

</div>

# Аксиомите чрез `flatMap`

* асоциативност:
  
  Нека `m: F[A]` и f: A => B, g: B => C. Тогава
  
  ```
  m.flatMap(f).flatMap(g) == m.flatMap(a => f(a).flatMap(g))
  ```
* ляв идентитет:
  
  ```
  ∀a: A и f: A => B е изпълнено: unit(a).flatMap(f) == f(a)
  ```
* десен идентитет: 
  
  ```
  ∀m: F[A] е изпълнено: m.flatMap(unit) == m 
  ```

# `for` в Scala е монадна композиция

```scala
for {
  b <- f(a)
  c <- g(b * 2)
  d <- h(b + c)
} yield a * b * d
```

преобразува се до:

```scala
f(a).flatMap(b =>
  g(b * 2).flatMap(c =>
    h(b + c).map(d => a * b * d))
}
```

# State монада

# Генерализация на монадите – функтори

```scala
trait Functor[F[_]]  {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
```

<div class="fragment">

```scala
trait Monad[F[_]] extends Functor[F] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def unit[A](a: A): F[A]

  def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(a => unit(f(a))
}
```

</div>

# Еквиваленти в Cats

# Композитност на функтори и монади

<div class="fragment">

Функторите могат да бъдат композирани:

```scala
val listOfOptions = List(Some(1), None, Some(2))
Functor[List].compose[Option].map(listOfOptions)(_ + 1)
```

</div>

<p class="fragment">В общия случай монадите не могат да се композират. Но много могат</p>

<p class="fragment">Това води до нуждата от специфични монадни трансформатори</p>

<p class="fragment">Например [`OptionT`](https://typelevel.org/cats/datatypes/optiont.html) за монади от `Option`<br />(тоест `M[Option[_]]`, където `M` е монада)</p>

# Нули на монади

`mZero: F[A]` наричаме нула за монадата `F`, ако:

:::incremental

* ∀ f: A → F[B] е изпълнено:
  
  ```scala
  flatMap(mZero)(f) == mZero
  ``` 
* ∀m ∈ F[A], която не е нула, е изпълнено:
  
  ```scala
  flatMap(m)(x => mZero) == mZero
  ```
:::

# MonadFilter

```scala
trait MonaFilter[F[_]] extends Monad[F] {
  def mzero[A]: F[A]
  def filter[A](m: F[A])(f: A => Boolean): F[A] =
    flatMap(m) { x => if (f(x)) unit(x) else mzero }
}
```

# Монада – аксиоми

* асоциативност:
  
  ```
  compose(compose(f, g), h) == compose(f, compose(g, h))
  ```
* неутрален елемент 
  
  ```
  compose(unit, f) == compose(f, unit) == f
  ```

<p class="fragment">Много прилича на моноиди</p>

# Теория на категориите

[![](images/12-monads-and-functors/47271389-8eea0900-d581-11e8-8e81-5b932e336336.png){ height="520" }](https://github.com/hmemcpy/milewski-ctfp-pdf)

# Functional Programming in Scala

[![](images/12-monads-and-functors/functional-programming-in-scala.jpg){ height="520" }](https://www.manning.com/books/functional-programming-in-scala)
