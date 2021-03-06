import PlayCrossCompilation.{dependencies, version}
import play.core.PlayVersion

val scala_2_11 = "2.11.12"
val scala_2_12 = "2.12.8"

lazy val majorVersionNumber = 0

lazy val lib = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(inConfig(Test)(testSettings): _*)
  .settings(commonSettings: _*)
  .settings(PlayCrossCompilation.playCrossCompilationSettings: _*)
  .settings(
    name := "play-nunjucks-viewmodel",
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++= libDependencies
  )

lazy val libDependencies: Seq[ModuleID] = dependencies(
  shared = {

    val compile = Seq(
      "org.typelevel"        %% "cats-core"            % "1.6.1",
      "com.typesafe.play"    %% "play"                 % version % "provided",
      "com.typesafe.play"    %% "filters-helpers"      % version % "provided",
      "com.github.pathikrit" %% "better-files"         % "3.5.0",
      "io.apigee.trireme"     % "trireme-core"         % "0.9.4",
      "io.apigee.trireme"     % "trireme-kernel"       % "0.9.4",
      "io.apigee.trireme"     % "trireme-node12src"    % "0.9.4",
      "org.webjars"           % "webjars-locator-core" % "0.35"
    )

    val test = Seq(
      "com.typesafe.play" %% "play-test"  % version,
      "org.scalactic"     %% "scalactic"  % "3.0.7",
      "org.scalatest"     %% "scalatest"  % "3.0.7",
      "org.scalacheck"    %% "scalacheck" % "1.14.0",
      "org.scalamock"     %% "scalamock"  % "4.1.0",
      "org.pegdown"        % "pegdown"    % "1.6.0"
    ).map(_ % Test)

    compile ++ test
  },
  play26 = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
  )
)

(test in (lib.project, Test)) := {
  (test in (lib.project, Test)).value
  (test in (itServer.project, Test)).value
}

lazy val itServer = (project in file("it-server"))
  .enablePlugins(PlayScala, SbtWeb)
  .dependsOn(lib)
  .settings(inConfig(Test)(testSettings): _*)
  .settings(commonSettings: _*)
  .settings(
    name := "it-server",
    libraryDependencies ++= PlayCrossCompilation.dependencies(
      shared = Seq(
        filters,
        "org.scalactic"  %% "scalactic"      % "3.0.7"  % "test",
        "org.scalatest"  %% "scalatest"      % "3.0.7"  % "test",
        "org.scalacheck" %% "scalacheck"     % "1.14.0" % "test",
        "org.pegdown"     % "pegdown"        % "1.6.0"  % "test",
        "org.webjars.npm" % "govuk-frontend" % "3.3.0"
      ),
      play26 = Seq(
        "com.typesafe.play"      %% "play-guice"         % PlayVersion.current,
        "uk.gov.hmrc"            %% "play-nunjucks"      % "0.26.0-play-26",
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
      ),
      play27 = Seq(
        "com.typesafe.play"      %% "play-guice"         % PlayVersion.current,
        "uk.gov.hmrc"            %% "play-nunjucks"      % "0.26.0-play-27",
        "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % "test"
      )
    ),
    Concat.groups := Seq(
      "javascripts/application.js" -> group(Seq("lib/govuk-frontend/govuk/all.js"))
    ),
    uglifyCompressOptions := Seq("unused=false", "dead_code=false"),
    pipelineStages in Assets := Seq(concat, uglify)
  )

lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  fork := true,
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
  isPublicArtefact := true,
  scalaVersion := scala_2_11,
  crossScalaVersions := Seq(scala_2_11, scala_2_12),
  scalacOptions ++= (Seq(
    "-deprecation"
  ) ++ CrossVersion.partialVersion(scalaVersion.value) match {
    case scala_2_12 => Nil
    case _          => Seq("-Xfatal-warnings")
  }),
  resolvers ++= Seq(
    Resolver.typesafeRepo("releases"),
    Resolver.jcenterRepo,
    "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases"
  )
)
