package homework2.io

case class IO[A](unsafeRun: () => A) {
  def map[B](f: A => B) = IO(() => f(this.unsafeRun()))
  def flatMap[B](f: A => IO[B]): IO[B] =
    IO(() => f(this.unsafeRun()).unsafeRun())
}
