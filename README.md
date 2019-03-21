# Функционално програмиране за напреднали със Scala

## Лекции

- [01 - За курса](https://scala-fmi.github.io/scala-fmi-2019/lectures/01-intro.html) \[[примерен код](lectures/examples/01-intro)\]
- [02 - Въведение в езика Scala](https://scala-fmi.github.io/scala-fmi-2019/lectures/02-scala-intro.html) \[[код](lectures/02-scala-intro-code.txt)\]
- [03 - Обектно ориентирано програмиране за отчаяни](https://scala-fmi.github.io/scala-fmi-2019/lectures/03-oop.html) \[[код](lectures/code/03/)\]
- [04 - Основи на функционалното програмиране](https://scala-fmi.github.io/scala-fmi-2019/lectures/04-functional-programming-basics.html) \[[код](lectures/04-functional-programming-basics-code.txt)\]

## Build-ване

### Setup

Имате нужда от инсталиран [pandoc](https://pandoc.org/installing.html).

Проектът има submodule зависимост към reveal.js. При/след клониране на репото инициализирайте модулите:

    git submodule update --init

### Генериране на лекция

    cd lectures
    ./generate-presentation.sh <лекция>

### Генериране на всички лекции

    cd lectures
    ./build.sh

### Стартиране на REPLesent script

Свалете пакета REPLesent от https://github.com/marconilanna/REPLesent и го настройте според инструкциите.

Сложете script файловете (*.txt) в директорията на REPLesent и след като го стартирате, изпълнете в конзолата командите:

    val replesent = REPLesent(source="02-scala-intro-code.txt",intp=$intp)
    import replesent._

като използвате подходящия файл за съответния script.
