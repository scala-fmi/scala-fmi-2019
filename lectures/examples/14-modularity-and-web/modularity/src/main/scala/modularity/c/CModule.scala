package modularity.c

import modularity.a.A3
import modularity.b.B2

trait CModule {
  def a3: A3
  def b2: B2

  lazy val c1 = new C1
  lazy val c2 = new C2(a3, b2, c1)
}
