# play-nunjucks-viewmodel

This library accompanies [play-nunjucks](https://github.com/hmrc/play-nunjucks).
It provides reusable view models and mappings to interact with Nunjucks pages, as well as worked examples.

## Play version
This library is currently compiled for Play 2.6 only.
Set the environment variable `PLAY_VERSION` to `2.6` before running an sbt command.

## Example server
An it-server project has been included as a reference, providing examples of using Nunjucks and the viewmodels.
You can see this in action by running the following command:
```sbt
PLAY_VERSION=2.X sbt "project itServer" run
```

## Installation

```sbt
libraryDependencies += "uk.gov.hmrc" %% "play-nunjucks-viewmodel" % "x.y.z-play-26"
```
