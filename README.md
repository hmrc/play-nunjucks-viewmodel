# play-nunjucks-viewmodel

This library accompanies [play-nunjucks](https://github.com/hmrc/play-nunjucks).
It provides reusable view models and mappings to interact with Nunjucks pages, as well as worked examples.

## Play version
This library can be compiled for both Play 2.5 and Play 2.6.
Simply set the environment variable `PLAY_VERSION` to `2.5` or `2.6` appropriately before running an sbt command.

## Example server
An it-server project has been included as a reference, providing examples of using Nunjucks and the viewmodels.
You can see this in action by running the following command:
```sbt
PLAY_VERSION=2.X sbt "project itServer" run
```

## Installation

```sbt
// replace suffix play-26 with play-25 if using with Play 2.5
libraryDependencies += "uk.gov.hmrc" %% "play-nunjucks-viewmodel" % "x.y.z-play-26"
```
