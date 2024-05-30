/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import better.files._
import org.webjars.{WebJarAssetLocator, WebJarExtractor}
import play.Environment

// can run in intellij or via cli with `sbt "project it-server-play-30" "runMain ListAssets"`

object ListAssets extends App {
  val outputFolder = File("webjar-locator-core-0-58").createDirectoryIfNotExists().clear()
  val listOfAssets = outputFolder / "assets.txt"
  new WebJarAssetLocator().listAssets("govuk-frontend").forEach(listOfAssets.appendLine)
  new WebJarExtractor(classOf[Environment].getClassLoader)
    .extractAllWebJarsTo((outputFolder / "assets").toJava)
}
