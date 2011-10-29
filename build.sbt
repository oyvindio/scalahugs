name := "scalahugs"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "kvikshaug.no maven Repository" at "http://mvn.kvikshaug.no"

libraryDependencies ++= Seq(
  "kvikshaug" % "pircbot-patched" % "1.5.0",
  "log4j" % "log4j" % "1.2.16"
)
