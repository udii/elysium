import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName = "elysium"
  val appVersion = "0.1-SNAPSHOT"

  scalaVersion := "2.10.2"

  val appDependencies = Seq(
    // Reactive Mongo dependencies
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10.0-SNAPSHOT",
    
    // WebJars pull in client-side web libraries
    "org.webjars" %% "webjars-play" % "2.2.1",
    "org.webjars" % "bootstrap" % "3.0.0",
    "org.webjars" % "knockout" % "3.0.0",
    "org.webjars" % "requirejs" % "2.1.8"
)

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
      // settings
  )

}
