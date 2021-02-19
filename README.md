# play-nunjucks-viewmodel

This library accompanies [play-nunjucks](https://github.com/hmrc/play-nunjucks).
It provides reusable view models and mappings to interact with Nunjucks pages, as well as worked examples.

## Play version
This library is currently compiled for Play 2.6 and Play 2.7.
Set the environment variable `PLAY_VERSION` to `2.6` or `2.7` appropriately before running an sbt command.

## Example server
An it-server project has been included as a reference, providing examples of using Nunjucks and the viewmodels.
You can see this in action by running the following command:
```sbt
PLAY_VERSION=2.7 sbt "project itServer" run
```

## Installation

```sbt
// replace suffix play-27 with play-26 if using with Play 2.6
libraryDependencies += "uk.gov.hmrc" %% "play-nunjucks-viewmodel" % "x.y.z-play-26"
```
