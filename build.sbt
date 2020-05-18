resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

lazy val commonSettings = Seq(
// Refine scalac params from tpolecat
  scalacOptions --= Seq(
    "-Xfatal-warnings"
  ),
  scalacOptions ++= Seq(
    "-Xsource:2.11",
    "-language:reflectiveCalls",
    "-language:postfixOps",
    "-unchecked",
    "-deprecation"
  )
)

lazy val chiselDeps = libraryDependencies ++= Seq(
  "edu.berkeley.cs" %% "chisel3" % Version.chisel,
  "edu.berkeley.cs" %% "firrtl"  % Version.firrtl
)

lazy val root = (project in file("."))
  .settings(
    organization := "Neurodyne",
    name := "tilelink",
    version := "0.0.1",
    scalaVersion := "2.12.10",
    maxErrors := 3,
    commonSettings,
    chiselDeps
  )

// Aliases
addCommandAlias("rel", "reload")
addCommandAlias("com", "all compile test:compile it:compile")
addCommandAlias("fix", "all compile:scalafix test:scalafix")
addCommandAlias("fmt", "all scalafmtSbt")

scalafixDependencies in ThisBuild += "com.nequissimus" %% "sort-imports" % "0.5.0"
