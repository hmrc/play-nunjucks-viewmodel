ThisBuild / majorVersion := 1
ThisBuild / isPublicArtefact := true
ThisBuild / organization := "uk.gov.hmrc"

val scala2_12 = "2.12.18"
val scala2_13 = "2.13.12"

def copySources(module: Project) = Seq(
  Compile / scalaSource := (module / Compile / scalaSource).value,
  Compile / resourceDirectory := (module / Compile / resourceDirectory).value,
  Test / scalaSource := (module / Test / scalaSource).value,
  Test / resourceDirectory := (module / Test / resourceDirectory).value
)

def scalacOptionsVersion(scalaVersion: String) =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, 12)) => Seq("-Ypartial-unification")
    case _             => Nil
  }

lazy val library = (project in file("."))
  .settings(publish / skip := true)
  .aggregate(
    sys.env.get("PLAY_VERSION") match {
      case Some("2.8") => playNunjucksPlay28
      case Some("2.9") => playNunjucksPlay29
      case _           => playNunjucksPlay30
    }
  )

lazy val playNunjucksPlay30 = Project("play-nunjucks-viewmodel-play-30", file("play-nunjucks-viewmodel-play-30"))
  .enablePlugins(SbtWeb)
  .settings(inConfig(Test)(testSettings): _*)
  .settings(scalaVersion := scala2_13)
  .settings(
    libraryDependencies ++= LibDependencies.play30,
    buildInfoKeys ++= Seq[BuildInfoKey]("playVersion" -> LibDependencies.play30Version)
  )

lazy val playNunjucksPlay29 = Project("play-nunjucks-viewmodel-play-29", file("play-nunjucks-viewmodel-play-29"))
  .enablePlugins(SbtWeb)
  .settings(copySources(playNunjucksPlay30))
  .settings(inConfig(Test)(testSettings): _*)
  .settings(scalaVersion := scala2_13)
  .settings(
    libraryDependencies ++= LibDependencies.play29
  )

lazy val playNunjucksPlay28 = Project("play-nunjucks-viewmodel-play-28", file("play-nunjucks-viewmodel-play-28"))
  .enablePlugins(SbtWeb)
  .settings(copySources(playNunjucksPlay30))
  .settings(inConfig(Test)(testSettings): _*)
  .settings(
    scalaVersion := scala2_12,
    crossScalaVersions := Seq(scala2_12, scala2_13),
    scalacOptions := scalacOptionsVersion(scalaVersion.value)
  )
  .settings(
    libraryDependencies ++= LibDependencies.play28
  )

lazy val itServer = sys.env.get("PLAY_VERSION") match {
  case Some("2.8") => itServerPlay28
  case Some("2.9") => itServerPlay29
  case _           => itServerPlay30
}

lazy val itServerPlay30 = Project("it-server-play-30", file("it-server-play-30"))
  .enablePlugins(PlayScala, SbtWeb)
  .dependsOn(playNunjucksPlay30)
  .settings(inConfig(Test)(testSettings): _*)
  .settings(sharedItServerSettings: _*)
  .settings(scalaVersion := scala2_13)
  .settings(libraryDependencies ++= LibDependencies.itServerPlay30)

lazy val itServerPlay29 = Project("it-server-play-29", file("it-server-play-29"))
  .enablePlugins(PlayScala, SbtWeb)
  .dependsOn(playNunjucksPlay29)
  .settings(copySources(itServerPlay30))
  .settings(inConfig(Test)(testSettings): _*)
  .settings(sharedItServerSettings: _*)
  .settings(scalaVersion := scala2_13)
  .settings(libraryDependencies ++= LibDependencies.itServerPlay29)

lazy val itServerPlay28 = Project("it-server-play-28", file("it-server-play-28"))
  .enablePlugins(PlayScala, SbtWeb)
  .dependsOn(playNunjucksPlay28)
  .settings(copySources(itServerPlay30))
  .settings(inConfig(Test)(testSettings): _*)
  .settings(sharedItServerSettings: _*)
  .settings(
    scalaVersion := scala2_12,
    crossScalaVersions := Seq(scala2_12, scala2_13)
  )
  .settings(libraryDependencies ++= LibDependencies.itServerPlay28)

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

library.project / Test / test := {
  (library.project / Test / test).value
  (itServer.project / Test / test).value
}
