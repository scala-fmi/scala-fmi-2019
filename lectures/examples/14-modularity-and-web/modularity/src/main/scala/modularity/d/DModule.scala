package modularity.d

import com.typesafe.config.Config
import modularity.Application.config
import modularity.{D1, D2}

trait DModule {
  def config: Config

  // Runtime decision based on config:
  lazy val d = {
    val dVersion = Option(config.getInt("application.d-version")).getOrElse(2)
    if (dVersion == 1) new D1 else new D2
  }
}
