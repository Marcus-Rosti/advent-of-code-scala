ThisBuild / organization := "com.codecommit"

ThisBuild / baseVersion := "0.1"

ThisBuild / publishGithubUser := "Marcus-Rosti"
ThisBuild / publishFullName := "Marcus Rosti"

ThisBuild / scalaVersion := "3.2.1"

lazy val `advent-of-code` = (project in file(".")).settings(
  name := "advent-of-code",
  Compile / mainClass := Some("com.mrosti.advent.Main"),
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.4.0",
    "org.typelevel" %% "cats-core" % "2.9.0",
    "org.typelevel" %% "log4cats-core" % "2.5.0",
    "org.typelevel" %% "log4cats-slf4j" % "2.5.0",
    "org.slf4j" % "slf4j-api" % "2.0.3",
    "org.slf4j" % "slf4j-simple" % "2.0.3",
    "co.fs2" %% "fs2-core" % "3.3.0",
    "co.fs2" %% "fs2-io" % "3.3.0",
    "ch.qos.logback" % "logback-classic" % "1.0.13"
  )
)
