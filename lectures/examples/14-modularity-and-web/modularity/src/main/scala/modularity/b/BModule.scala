package modularity.b

import modularity.a.{A1, AModule}

trait BModule {
  def a1: A1

  lazy val b1 = new B1
  lazy val b2 = new B2(b1, a1)
}
