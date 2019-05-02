package homework2.io

import scala.io.StdIn

object Console {
  def putStrLn(line: String): IO[Unit] = IO(() => println(line))
  def getStrLn: IO[String] = IO(() => StdIn.readLine())
}
