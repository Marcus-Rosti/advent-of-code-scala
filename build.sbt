ThisBuild / organization := "com.codecommit"

ThisBuild / baseVersion := "0.1"

ThisBuild / publishGithubUser := "Marcus-Rosti"
ThisBuild / publishFullName := "Marcus Rosti"

ThisBuild / scalaVersion := "3.2.1"

val fs2Version = "3.3.0"
val catsVersion = "2.9.0"
val catsEffectVersion = "3.4.2"
val http4sVersion = "1.0.0-M37"

lazy val `advent-of-code` = (project in file("."))
  .enablePlugins(GraalVMNativeImagePlugin)
  .settings(
    name := "advent-of-code",
    scalacOptions := Seq("-explain"),
    Compile / mainClass := Some("com.mrosti.advent.Main"),
    graalVMNativeImageGraalVersion := Some("22.3.0"),
    graalVMNativeImageOptions ++= Seq(
      "--no-fallback",
      "--initialize-at-build-time=org.slf4j.LoggerFactory,ch.qos.logback",
      "-H:+ReportExceptionStackTraces",
    ),
    libraryDependencies ++= Seq(
      // Cats
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
      "org.typelevel" %% "cats-core" % catsVersion,

      // Streaming
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io" % fs2Version,

      // Http4s
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-client" % http4sVersion,
      "org.http4s" %% "http4s-ember-client" % http4sVersion,

      // Log
      "org.typelevel" %% "log4cats-core" % "2.5.0",
      "org.typelevel" %% "log4cats-slf4j" % "2.5.0",
      "ch.qos.logback" % "logback-classic" % "1.4.5",
      "org.slf4j" % "slf4j-api" % "2.0.5",
      // "org.slf4j" % "slf4j-simple" % "2.0.5",
      "org.fusesource.jansi" % "jansi" % "2.4.0",
    )
  )
