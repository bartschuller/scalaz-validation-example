name := "scalaz validation example"

version := "0.1.0-SNAPSHOT"

organization := "org.smop"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "6.0.4",
  "org.specs2" %% "specs2" % "1.8.2" % "test"
)

// test debug output gets mangled otherwise
parallelExecution in Test := false

scalacOptions := Seq("-Yrepl-sync")

