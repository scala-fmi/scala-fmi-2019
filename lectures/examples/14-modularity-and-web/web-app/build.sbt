name := "web-app"
version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  ws, // Web client library, coming from the Play Framework
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test
)
