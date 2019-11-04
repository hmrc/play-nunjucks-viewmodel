import play.core.PlayVersion

lazy val majorVersionNumber = 0

lazy val lib = (project in file("."))
  .enablePlugins(SbtWeb, SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(inConfig(Test)(testSettings): _*)
  .settings(commonSettings: _*)
  .settings(
    name := "play-nunjucks-viewmodel",
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++= Seq(
      "org.typelevel"     %% "cats-core"       % "1.6.1",
      "com.typesafe.play" %% "play"            % PlayVersion.current % "test, provided",
      "com.typesafe.play" %% "play-test"       % PlayVersion.current % "test",
      "com.typesafe.play" %% "filters-helpers" % PlayVersion.current % "test, provided",
      "org.scalactic"     %% "scalactic"       % "3.0.7"             % "test",
      "org.scalatest"     %% "scalatest"       % "3.0.7"             % "test",
      "org.scalacheck"    %% "scalacheck"      % "1.14.0"            % "test",
      "org.scalamock"     %% "scalamock"       % "4.1.0"             % "test",
      "org.pegdown"       %  "pegdown"         % "1.6.0"             % "test"
    )
  )

(test in(lib.project, Test)) := {
  (test in(lib.project, Test)).value
  (test in(itServer.project, Test)).value
}

lazy val itServer = (project in file("it-server"))
  .enablePlugins(PlayScala, SbtWeb)
  .dependsOn(lib)
  .settings(inConfig(Test)(testSettings): _*)
  .settings(commonSettings: _*)
  .settings(
    name := "it-server",
    libraryDependencies ++= Seq(
      filters,
      "org.scalactic"          %% "scalactic"           % "3.0.7"             % "test",
      "org.scalatest"          %% "scalatest"           % "3.0.7"             % "test",
      "org.scalacheck"         %% "scalacheck"          % "1.14.0"            % "test",
      "org.pegdown"            % "pegdown"              % "1.6.0"             % "test",
      "org.scalatestplus.play" %% "scalatestplus-play"  % "2.0.1"             % "test",
      "com.typesafe.play"      %% "play-guice"          % PlayVersion.current,
      "uk.gov.hmrc"            %% "play-nunjucks"       % "0.19.0-play-26",
      "org.webjars.npm"        %  "govuk-frontend"      % "3.3.0"
    ),
    Concat.groups := Seq(
      "javascripts/application.js" -> group(Seq("lib/govuk-frontend/govuk/all.js"))
    ),
    uglifyCompressOptions := Seq("unused=false", "dead_code=false"),
    pipelineStages in Assets := Seq(concat,uglify)
  )

lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  fork        := true,
  javaOptions ++= Seq(
    "-Dconfig.resource=test.application.conf"
  ),
  unmanagedSourceDirectories ++= Seq(
    baseDirectory.value / "test-utils"
  )
)

lazy val commonSettings: Seq[Def.Setting[_]] = Seq(
  organization := "uk.gov.hmrc",
  majorVersion := majorVersionNumber,
  makePublicallyAvailableOnBintray := true,
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.11.12"),
  scalacOptions ++= Seq(
    "-Xfatal-warnings",
    "-deprecation"
  ),
  resolvers ++= Seq(
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.bintrayRepo("hmrc", "snapshots"),
    Resolver.bintrayRepo("hmrc", "release-candidates"),
    Resolver.typesafeRepo("releases"),
    Resolver.jcenterRepo,
    "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases"
  )
)
