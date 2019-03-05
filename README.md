# Функционално програмиране за напреднали със Scala

## Лекции

- [01 - За курса](https://scala-fmi.github.io/scala-fmi-2019/lectures/01-intro.html) \[[примерен код](lectures/examples/01-intro)\]
- [02 - Въведение в езика Scala](https://scala-fmi.github.io/scala-fmi-2019/lectures/02-scala-intro.html) \[[код](lectures/02-scala-intro-code.txt)\]

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
