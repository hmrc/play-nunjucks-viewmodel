import sbt._

object LibDependencies {

  private val shared = Seq(
    "org.typelevel" %% "cats-core" % "2.2.0"
  )

  val play30 = Seq(
    "uk.gov.hmrc"            %% s"play-nunjucks-play-30" % s"1.12.0",
    "com.vladsch.flexmark"    % "flexmark-all"           % "0.64.8" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.0"  % Test
  ) ++ shared

  private val itServerShared = Seq(
    "org.webjars.npm" % "govuk-frontend" % "4.8.0"
  )

  val itServerPlay30 = Seq(
    "org.playframework" %% "play-guice" % "3.0.3"
  ) ++ itServerShared
}
