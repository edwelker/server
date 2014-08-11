name := "server"

version := "0.1"

scalaVersion := "2.10.3"

resolvers += "twtter" at "http://maven.twttr.com/"

libraryDependencies += "com.twitter" %% "twitter-server" % "1.7.3"

net.virtualvoid.sbt.graph.Plugin.graphSettings

libraryDependencies += "com.twitter" %% "finagle-stats" % "6.20.0"
