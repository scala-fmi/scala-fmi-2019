object Console {
  def putStrLn(line: String): IO[Unit] = IO(() => println(line))
  def getStrLn: IO[String] = IO(() => readLine())
}