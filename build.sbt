ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "scala-design-patterns"
  )

libraryDependencies += "commons-codec" % "commons-codec" % "1.17.1"