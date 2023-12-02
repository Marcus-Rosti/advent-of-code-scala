// huh, https://github.com/scala/bug/issues/12632
ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)

// for stock functionality (no publication defaults)
addSbtPlugin("com.codecommit" % "sbt-spiewak" % "0.23.0")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9")
addSbtPlugin("org.scalameta" % "sbt-native-image" % "0.3.2")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "1.2.7")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.0.0")