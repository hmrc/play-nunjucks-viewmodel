
# play-nunjucks-viewmodel

This library accompanies [play-nunjucks](https://github.com/hmrc/play-nunjucks). It includes an it-server project which gives examples of using nunjucks and the viewmodels, as a useful reference.

## Installation

To publish the library locally, clone this repository and run:

```bash
sbt lib/publishLocal
```

Once you have done this you can include it in a local project
by adding the following to your `build.sbt` file:

```scala
libraryDependencies += "uk.gov.hmrc" %% uk.govuk.gov.hmrc.nunjucksnjucks % "0.1.0-SNAPSHOT"

