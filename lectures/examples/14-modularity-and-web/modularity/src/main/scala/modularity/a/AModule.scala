package modularity.a

trait AModule {
  lazy val a3 = new A3(a1, a2)
  lazy val a1 = new A1
  lazy val a2 = new A2
}
