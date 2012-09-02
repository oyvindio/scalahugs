name := "scalahugs"

version := "1.0"

scalaVersion := "2.9.2"

resolvers += "kvikshaug.no maven Repository" at "http://mvn.kvikshaug.no"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "kvikshaug" % "pircbot-patched" % "1.5.0",
  "com.typesafe.akka" % "akka-actor" % "2.0.3",
  "com.typesafe.akka" % "akka-slf4j" % "2.0.3",
  "ch.qos.logback" % "logback-classic" % "1.0.7",
  "org.slf4j" % "slf4j-api" % "1.7.0"
)
