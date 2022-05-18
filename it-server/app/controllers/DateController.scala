/*
 * Copyright 2022 HM Revenue & Customs
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

import java.time.LocalDate

import com.google.inject.Inject
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.mappings.LocalDateMapping
import uk.gov.hmrc.nunjucks.NunjucksRenderer

import scala.concurrent.{ExecutionContext, Future}

class DateController @Inject()(
                                renderer: NunjucksRenderer,
                                cc: ControllerComponents
                              )(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  import uk.gov.hmrc.viewmodels._

  private val emptyForm: Form[LocalDate] = Form(
    "date" -> new LocalDateMapping()
  )

  def get: Action[AnyContent] = Action.async {
    implicit request =>

      val existingValue = getFromSession[LocalDate]("date")
      val form = existingValue.map(emptyForm.fill).getOrElse(emptyForm)

      val viewModel = DateInput.localDate(form("date"))

      renderer.render("date.njk", Json.obj(
        "form" -> form,
        "date" -> viewModel
      )).map(Ok(_))
  }

  def post: Action[AnyContent] = Action.async {
    implicit request =>


      emptyForm.bindFromRequest().fold(
        errors => {

          val viewModel = DateInput.localDate(errors("date"))

          renderer.render("date.njk", Json.obj(
            "form" -> errors,
            "date" -> viewModel
          )).map(BadRequest(_))
        },
        value =>
          Future.successful {
            Redirect(controllers.routes.DateController.get())
              .addingToSession("date" -> Json.stringify(Json.toJson(value)))
          }
      )
  }
}
