name := "scalahugs"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "kvikshaug.no maven Repository" at "http://mvn.kvikshaug.no"

libraryDependencies ++= Seq("kvikshaug" % "pircbot-patched" % "1.5.0")

resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "0.11.0")

