name := "akka-router"

version := "0.1"

scalaVersion := "2.12.5"


resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "http://oss.sonatype.org/content/repositories/releases",
  "maven central" at "https://mvnrepository.com/repos/central"
)

libraryDependencies += "com.typesafe.akka" %% "akka-http"   % "10.1.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.11" // or whatever the latest version is
libraryDependencies += "de.heikoseeberger" %% "akka-http-json4s" % "1.20.0"
libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.0-M2"
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.6.0-M2"