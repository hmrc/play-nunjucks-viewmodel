# play-nunjucks-viewmodel

This library accompanies [play-nunjucks](https://github.com/hmrc/play-nunjucks).
It provides reusable view models and mappings to interact with Nunjucks pages, as well as worked examples.

## Play version
This library is currently compiled for Play 2.6, Play 2.7, and Play 2.8.
Set the environment variable `PLAY_VERSION` to `2.6` or `2.7` or `2.8` appropriately before running an sbt command.

## Example server
An it-server project has been included as a reference, providing examples of using Nunjucks and the viewmodels.
You can see this in action by running the following command:
```sbt
PLAY_VERSION=2.8 sbt "project itServer" run
```

## Installation

```sbt
// replace suffix play-28 with play-27 or play-26 if using older Play version
libraryDependencies += "uk.gov.hmrc" %% "play-nunjucks-viewmodel" % "x.y.z-play-28"
```
