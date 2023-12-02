ThisBuild / organization := "com.codecommit"

ThisBuild / baseVersion := "0.1.2023"

ThisBuild / publishGithubUser := "Marcus-Rosti"
ThisBuild / publishFullName   := "Marcus Rosti"

ThisBuild / scalaVersion := "3.3.1"

val fs2Version        = "3.9.3"
val catsVersion       = "2.10.0"
val catsEffectVersion = "3.5.2"
val http4sVersion     = "0.23.24"
val log4catsVersion   = "2.5.0"

lazy val `advent-of-code` = (project in file("."))
  .enablePlugins(GraalVMNativeImagePlugin, NativeImagePlugin)
  .settings(
    name                := "advent-of-code",
    scalacOptions       := Seq("-explain", "-source:future"),
    Compile / mainClass := Some("com.mrosti.advent.Main"),
    libraryDependencies ++= Seq(
      // Cats
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
      "org.typelevel" %% "cats-core"   % catsVersion,
      "org.typelevel" %% "cats-parse"  % "1.0.0",

      // Streaming
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io"   % fs2Version,

      // Http4s
      "org.http4s" %% "http4s-core"         % http4sVersion,
      "org.http4s" %% "http4s-client"       % http4sVersion,
      "org.http4s" %% "http4s-ember-client" % http4sVersion,

      // Log
      "org.typelevel" %% "log4cats-core"   % log4catsVersion,
      "org.typelevel" %% "log4cats-slf4j"  % log4catsVersion,
      "ch.qos.logback" % "logback-classic" % "1.4.7",
      "org.slf4j"      % "slf4j-api"       % "2.0.5",
      // "org.slf4j" % "slf4j-simple" % "2.0.5",
      "org.fusesource.jansi" % "jansi" % "2.4.0"
    )
  )
