name := "scalahugs"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "kvikshaug.no maven Repository" at "http://mvn.kvikshaug.no"

libraryDependencies ++= Seq(
  "kvikshaug" % "pircbot-patched" % "1.5.0",
  "org.slf4j" % "slf4j-simple" % "1.6.1",
  "com.weiglewilczek.slf4s" %% "slf4s" % "1.0.7"
)



