name := "server"

version := "0.1"

scalaVersion := "2.10.3"

resolvers += "twtter" at "http://maven.twttr.com/"

libraryDependencies += "com.twitter" % "twitter-server_2.10" % "1.7.3"

net.virtualvoid.sbt.graph.Plugin.graphSettings

//libraryDependencies += "com.twitter" %% "twitter-server" % "1.0.2" //exclude("com.twitter", "util-core_2.10")

//libraryDependencies += "com.twitter" % "util-core_2.10" % "0.1-SNAPSHOT"
