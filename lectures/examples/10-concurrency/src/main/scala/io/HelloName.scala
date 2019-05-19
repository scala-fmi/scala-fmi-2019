package io

object HelloName extends App {
  import Console._

  val program = for {
    _     <- putStrLn("What is your name?")
    name  <- getStrLn
    _     <- putStrLn("Hello, " + name + ", welcome!")
  } yield ()

  program.unsafeRun()
}
