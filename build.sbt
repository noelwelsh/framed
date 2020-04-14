val dottyVersion = dottyLatestNightlyBuild.get //"0.24.0-bin-SNAPSHOT"

ThisBuild / useSuperShell := false

lazy val root = project
  .in(file("."))
  .settings(
    name := "framed",
    version := "0.1.0",

    scalaVersion := dottyVersion,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
