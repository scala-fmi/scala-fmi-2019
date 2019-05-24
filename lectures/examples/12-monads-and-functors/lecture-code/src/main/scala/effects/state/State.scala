package effects.state

import effects.Monad

case class State[S, A](run: S => (S, A))

object State {
  implicit def stateMonad[S] = new Monad[({type T[A] = State[S, A]})#T] {
    def flatMap[A, B](fa:  State[S, A])(f:  A => State[S, B]): State[S, B] = State { s1 =>
      val (s2, a) = fa.run(s1)
      f(a).run(s2)
    }

    def unit[A](a: => A): State[S, A] = State(s => (s, a))
  }
}
