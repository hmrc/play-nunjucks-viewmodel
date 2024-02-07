# play-nunjucks-viewmodel

This library accompanies [play-nunjucks](https://github.com/hmrc/play-nunjucks).
It provides reusable view models and mappings to interact with Nunjucks pages, as well as worked examples.

## Play version
This library is compatible with Play 2.8, Play 2.9 and Play 3.0

Set the environment variable `PLAY_VERSION` to `2.8` or `2.9` or `3.0` appropriately before running an sbt command.

## Example server
An it-server project has been included as a reference, providing examples of using Nunjucks and the viewmodels.
You can see this in action by running the following command:
```sbt
PLAY_VERSION=3.0 sbt "project it-server-play-30" run
```

## Installation

```sbt
// replace suffix play-28 with play-27 or play-26 if using older Play version
libraryDependencies += "uk.gov.hmrc" %% "play-nunjucks-viewmodel" % "x.y.z-play-28"
```
