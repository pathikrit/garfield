name := "garfield"

version := "0.0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.sbtPluginRepo("snapshots"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.typesafeRepo("snapshots"),
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeIvyRepo("releases")
)

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc4"
)
