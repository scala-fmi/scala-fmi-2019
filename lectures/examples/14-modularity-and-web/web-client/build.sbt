name := "web-client"
version := "0.1"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.0.4",
  "com.typesafe.play" %% "play-ws-standalone-json" % "2.0.4",

  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.slf4j" % "slf4j-simple" % "1.7.25",

  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
