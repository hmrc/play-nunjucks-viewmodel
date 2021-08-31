/*
 * Copyright 2021 HM Revenue & Customs
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

package controllers

import com.google.inject.Inject
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.nunjucks.NunjucksRenderer
import uk.gov.hmrc.viewmodels._

import scala.concurrent.ExecutionContext

class SummaryListController @Inject()(
                                       renderer: NunjucksRenderer,
                                       cc: ControllerComponents
                                     )(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  def get: Action[AnyContent] = Action.async {
    implicit request =>

      val summaryList: List[SummaryList.Row] = {

        List(
          SummaryList.Row(
            key     = SummaryList.Key(msg"radios.title"),
            value   = SummaryList.Value(getFromSession[RadioAnswer]("radios").map(value => msg"radios.${value.toString.toLowerCase}").getOrElse(msg"site.na")),
            actions = List(
              SummaryList.Action(
                msg"site.edit",
                routes.RadiosController.get().url,
                visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"radios.title"))
              )
            )
          ),
          SummaryList.Row(
            key     = SummaryList.Key(msg"yesNo.title"),
            value   = SummaryList.Value(if (getFromSession[Boolean]("yesNo").contains(true)) msg"site.yes" else msg"site.no"),
            actions = List(
              SummaryList.Action(
                msg"site.edit",
                routes.YesNoController.get().url,
                visuallyHiddenText = Some(msg"site.edit.hidden".withArgs(msg"yesNo.title"))
              )
            )
          )
        )
      }

      renderer.render("summaryList.njk", Json.obj(
        "list" -> summaryList
      )).map(Ok(_))
  }
}
