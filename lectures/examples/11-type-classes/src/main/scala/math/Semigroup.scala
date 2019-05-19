package math

trait Semigroup[M] {
  def op(a: M, b: M): M
}

object Semigroup {
  def apply[A](implicit m: Semigroup[A]): Semigroup[A] = m

  object ops {
    implicit class SemigroupOps[A](val a: A) extends AnyVal {
      def |+|(b: A)(implicit m: Semigroup[A]) = m.op(a, b)
    }
  }
}