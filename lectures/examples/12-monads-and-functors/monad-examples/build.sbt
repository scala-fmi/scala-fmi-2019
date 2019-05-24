name := "MonadExamples"

version := "0.1"

scalaVersion := "2.12.8"

initialCommands in console := """import example._, MonadInstances._;import scala.concurrent.Future;import scala.concurrent.ExecutionContext.Implicits.global;"""

