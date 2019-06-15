package modularity

import com.typesafe.config.ConfigFactory
import modularity.a.{A3, AModule}
import modularity.b.BModule
import modularity.c.CModule
import modularity.d.DModule


object Application extends AModule
  with BModule
  with CModule
  with DModule {

  // Loading TypeSafe config from src/main/resources/application.conf
  // More info: https://github.com/lightbend/config
  lazy val config = ConfigFactory.load()

  def main(args: Array[String]): Unit = {
    println(c2.doSomething)
  }
}
