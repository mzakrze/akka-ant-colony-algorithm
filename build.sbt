name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.18"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

libraryDependencies ++= {
  val AkkaHttpVersion   = "2.0.1"
  val Json4sVersion     = "3.2.11"
  Seq(
    "com.typesafe.akka" %% "akka-http" % "10.1.5",
    "com.typesafe.akka" %% "akka-stream" % "2.5.12",
    //"com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",
    "ch.qos.logback"    %  "logback-classic" % "1.1.2",
    "org.json4s"        %% "json4s-native"   % Json4sVersion,
    "org.json4s"        %% "json4s-ext"      % Json4sVersion
  )
}