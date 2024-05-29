val scala2_13 = "2.13.12"

ThisBuild / majorVersion := 1
ThisBuild / isPublicArtefact := true
ThisBuild / organization := "uk.gov.hmrc"

lazy val playNunjucksPlay30 = Project("play-nunjucks-viewmodel-play-30", file("play-nunjucks-viewmodel-play-30"))
  .enablePlugins(SbtWeb)
  .settings(inConfig(Test)(testSettings): _*)
  .settings(scalaVersion := scala2_13)
  .settings(
    libraryDependencies ++= LibDependencies.play30,
    buildInfoKeys ++= Seq[BuildInfoKey]("playVersion" -> LibDependencies.play30Version)
  )

lazy val itServerPlay30 = Project("it-server-play-30", file("it-server-play-30"))
  .enablePlugins(PlayScala, SbtWeb)
  .dependsOn(playNunjucksPlay30)
  .settings(publish / skip := true)
  .settings(inConfig(Test)(testSettings): _*)
  .settings(sharedItServerSettings: _*)
  .settings(scalaVersion := scala2_13)
  .settings(libraryDependencies ++= LibDependencies.itServerPlay30)

lazy val sharedItServerSettings = Seq(
  Concat.groups := Seq(
    "javascripts/application.js" -> group(Seq("lib/govuk-frontend/govuk/all.js"))
  ),
  Assets / pipelineStages := Seq(concat, uglify)
)

lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  fork := true,
  javaOptions ++= Seq(
    "-Dconfig.resource=test.application.conf"
  )
)
