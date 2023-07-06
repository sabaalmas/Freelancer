lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := """Freelancelot""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.4",
    crossScalaVersions := Seq("2.11.12", "2.12.4","2.13.6"),
    libraryDependencies ++= Seq(
      guice,
      javaWs,
      cacheApi,
      ehcache,
      // Test Database
      "com.h2database" % "h2" % "1.4.199",
      // Testing libraries for dealing with CompletionStage...
      "org.assertj" % "assertj-core" % "3.14.0" % Test,
      "org.awaitility" % "awaitility" % "4.0.1" % Test,
      "org.mockito" % "mockito-core" % "2.10.0" % "test",
      //"org.apache.maven.plugins" % "maven-javadoc-plugin" % "3.3.2"
      //akka
      "org.webjars" %% "webjars-play" % "2.6.2",
      "org.webjars" % "flot" % "0.8.3",
      "org.webjars" % "bootstrap" % "3.3.6",
      "com.typesafe.akka" %% "akka-actor" % "2.5.3",
      "com.miguno.akka" % "akka-mock-scheduler_2.12" % "0.5.1",
      "com.typesafe.akka" %% "akka-testkit" % "2.6.18" % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    ),
    Test / jacocoExcludes := Seq(
      "controllers.Reverse*",
      "controllers.javascript.*",
      "jooq.*",
      "Module",
      "router.Routes*",
      "*.routes*",
      "*views.html*",
      "*model.*",
      "*ReadibilityHelper.*",
      "*UserDetailsHelper.*",
    ),
    javacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-parameters",
      "-Xlint:unchecked",
      "-Xlint:deprecation"
    ),
    //seq(javadoc.JavadocPlugin.javadocSettings: _*),
    //Compile / doc / javacOptions ++= Seq("-notimestamp", "-linksource"),
    //sources in (Compile, doc) <<= sources in (Compile, doc) map { _.filterNot(_.getName endsWith ".scala") },
    // Make verbose tests
    sources in (Compile, doc) ~= (_ filter (_.getName endsWith ".java")),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )
