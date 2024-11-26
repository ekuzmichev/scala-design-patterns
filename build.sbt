ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "scala-design-patterns"
  )

libraryDependencies ++= Seq(
  "commons-codec"         % "commons-codec"  % "1.17.1",
  "org.json4s"           %% "json4s-jackson" % "4.0.7",
  "com.github.tototoshi" %% "scala-csv"      % "2.0.0"
)
