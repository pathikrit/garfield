sbtPlugin := true

name := "play-slick-evolutions-plugin"

libraryDependencies ++= Seq(
  jdbc,
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "com.typesafe.slick" %% "slick-codegen" % "2.1.0"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
