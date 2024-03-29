name := "ProphetScala"

version := "0.0.1"

organization := "ru.mokc"

scalaVersion := "2.9.1"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases",
                "Spy Repository" at "http://files.couchbase.com/maven2/",
                "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
                )

seq(com.github.siasia.WebPlugin.webSettings :_*)

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5-M1"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftmodules"   %% "lift-jquery-module" % (liftVersion + "-1.0"),
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "1.12.1"           % "test",
    "com.googlecode.xmemcached" % "xmemcached"  % "1.3.2",
    "spy"               % "spymemcached"        % "2.8.1",
    "com.typesafe.akka" % "akka-actor"          % "2.0.3"
  )
}

