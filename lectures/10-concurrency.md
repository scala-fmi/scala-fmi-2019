---
title: Конкурентност
---
# Досега?

::: incremental

* Програми с мощността на ламбда смятането/машината на Тюринг
* Нямат връзка с околния свят
* вход =&gt; предвидима трансформация =&gt; изход
* Последователни изчисления, не се влияят от времето
* Трансформиращи програми
* Добре изучени

:::

# IO

```scala
import Console._

val program = for {
  _     <- putStrLn("What is your name?")
  name  <- getStrLn
  _     <- putStrLn("Hello, " + name + ", welcome!")
} yield ()

program.unsafeRun()
```

::: incremental

* Връзка с външния свят
* Но синхронна – програмата не прави нищо друго докато чака
* Интерактивни програми

:::

# Реалният свят

::: incremental

* Светът навън е силно паралелен
* Нещо повече, участниците в него си взаимодействат
* Как да моделираме такива програми?

:::

# Конкурентност и паралелизъм


<div class="align">
  <dl class="fragment">
    <dt>parallel</dt>
    <dd>from παρά + ἄλληλος, along each other</dd>
  </dl>
  
  <dl class="fragment">
    <dt>concurrent</dt>
    <dd>present active participle of concurrō (“happen at the same time with”), from con- (“with”) + currō (“run”)</dd>
  </dl>
  
  <dl class="fragment">
    <dt>concurrent computing</dt>
    <dd>a form of computing in which several computations are executed during overlapping time periods—concurrently—instead of sequentially</dd>
  </dl>
</div>

# Дистрибутираност

# Реактивност

# Конкурентност

В изчислителен контекст:

::: incremental
 
* _конкурентността_ се отнася към структурата на програмата,
* _паралелизмът_, _дистрибутираността_ – към хардуера и как тя ще бъде изпълнявана.

:::

<p class=fragment>Конкурентните програми са композитност от unit-и от изчисления, които, веднъж дефинирани, могат да бъдат изпълнени независимо едно от друго.</p>

# Конкурентни модели

# Нишки

# 

