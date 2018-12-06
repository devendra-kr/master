name := "Akka_Innovation"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test
)