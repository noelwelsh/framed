ThisBuild / scalaVersion := "3.0.0-RC1"
ThisBuild / useSuperShell := false

lazy val root = project
  .in(file("."))
  .settings(
    name := "framed",
    version := "0.1.0",

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
