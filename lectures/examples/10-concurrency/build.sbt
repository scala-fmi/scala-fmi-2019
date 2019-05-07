name := "concurrency"
version := "0.1"

libraryDependencies ++= Seq(
  "io.monix" %% "monix" % "2.3.3",
  "com.typesafe.akka" %% "akka-actor" % "2.5.22",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.5.22",
  "com.typesafe.akka" %% "akka-stream" % "2.5.22",
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "org.asynchttpclient" % "async-http-client" % "2.8.1",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
