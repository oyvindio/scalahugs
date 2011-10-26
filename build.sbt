name := "scalahugs"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "kvikshaug.no maven Repository" at "http://mvn.kvikshaug.no"

resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"

libraryDependencies ++= Seq("kvikshaug" % "pircbot-patched" % "1.5.0")



