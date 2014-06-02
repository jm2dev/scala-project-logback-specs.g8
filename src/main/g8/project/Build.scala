import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "$organization$"
  val buildVersion      = "$version$"
  val buildScalaVersion = "$scalaVersion$"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version       := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt  := ShellPrompt.buildShellPrompt,
    exportJars   := true
  )
}

// Shell prompt which show the current project,
// git branch and build version
object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
  )

  val buildShellPrompt = {
    (state: State) => {
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (
        currProject, currBranch, BuildSettings.buildVersion
      )
    }
  }
}

object Dependencies {
  // alphabetical order
  val groovyVersion = "$groovyVersion$"
  val logbackVersion = "$logbackVersion$"
  val slf4jVersion = "$slf4jApiVersion$"
  val specs2Version = "$specs2Version$"
  val typesafeConfigVersion = "$typesafeConfigVersion$"
  
  val groovy           = "org.codehaus.groovy" % "groovy" % groovyVersion withSources() withJavadoc()
  val logbackclassic   = "ch.qos.logback" % "logback-core" % logbackVersion withSources() withJavadoc()
  val logbackcore      = "ch.qos.logback" % "logback-classic" % logbackVersion withSources() withJavadoc()
  val slf4j            = "org.slf4j" % "slf4j-api" % slf4jVersion withSources() withJavadoc()
  val specs2           = "org.specs2" %% "specs2" % specs2Version % "test" withSources() withJavadoc()
  val typesafeConfig   = "com.typesafe" % "config" % typesafeConfigVersion withSources() withJavadoc()
}

object AppBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  // Sub-project specific dependencies
  val commonDeps = Seq(
    groovy,
    logbackclassic,
    logbackcore,
    slf4j,
    specs2,
    typesafeConfig)

  lazy val app = Project(
    id = "$name;format="snake-case"$",
    base = file("."),
    settings = buildSettings
  )aggregate(
    common)

  lazy val common = Project(
    id = "common",
    base = file("common"),
    settings = buildSettings ++ Seq(libraryDependencies ++= commonDeps))
}
