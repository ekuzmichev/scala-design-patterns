ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "scala-design-patterns"
  )

libraryDependencies ++= Seq(
  "commons-codec"               % "commons-codec"           % "1.17.1",
  "org.json4s"                 %% "json4s-native"           % "4.0.7",
  "org.json4s"                 %% "json4s-jackson"          % "4.0.7",
  "com.github.tototoshi"       %% "scala-csv"               % "2.0.0",
  "org.scalaz"                 %% "scalaz-core"             % "7.3.8",
  "com.h2database"              % "h2"                      % "2.3.232",
  "com.typesafe"                % "config"                  % "1.4.3",
  "com.typesafe.akka"          %% "akka-actor"              % "2.6.21",
  "org.slf4j"                   % "slf4j-log4j12"           % "2.0.16",
  "com.typesafe.scala-logging" %% "scala-logging"           % "3.9.5",
  "org.mockito"                %% "mockito-scala-scalatest" % "1.17.37" % Test,
  "org.scalatest"              %% "scalatest"               % "3.2.19"  % Test
)
