lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := """Freelancelot""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      guice,
      javaWs,
      // Test Database
      "com.h2database" % "h2" % "1.4.199",
      // Testing libraries for dealing with CompletionStage...
      "org.assertj" % "assertj-core" % "3.14.0" % Test,
      "org.awaitility" % "awaitility" % "4.0.1" % Test,
      "org.mockito" % "mockito-core" % "2.10.0" % "test"
      //"org.apache.maven.plugins" % "maven-javadoc-plugin" % "3.3.2"
    ),
    Test / jacocoExcludes := Seq(
      "controllers.Reverse*",
      "controllers.javascript.*",
      "jooq.*",
      "Module",
      "router.Routes*",
      "*.routes*",
      "*views.html*",
    ),
    javacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-parameters",
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Werror"
    ),
	javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    //seq(javadoc.JavadocPlugin.javadocSettings: _*),
    //Compile / doc / javacOptions ++= Seq("-notimestamp", "-linksource"),
    //sources in (Compile, doc) <<= sources in (Compile, doc) map { _.filterNot(_.getName endsWith ".scala") },
    // Make verbose tests
    sources in (Compile, doc) ~= (_ filter (_.getName endsWith ".java")),
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )
