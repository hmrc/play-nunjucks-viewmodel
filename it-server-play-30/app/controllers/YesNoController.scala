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

import com.google.inject.Inject
import play.api.data.format.Formatter
import play.api.data.{Form, FormError, Forms}
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.nunjucks.NunjucksRenderer

import scala.concurrent.{ExecutionContext, Future}

class YesNoController @Inject() (
  renderer: NunjucksRenderer,
  cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with I18nSupport {

  import uk.gov.hmrc.viewmodels._

  private implicit val formatter: Formatter[Boolean] =
    new Formatter[Boolean] {

      override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Boolean] =
        data.get(key) match {
          case Some("true")    => Right(true)
          case Some("false")   => Right(false)
          case Some("") | None => Left(Seq(FormError(key, "error.required")))
          case _               => Left(Seq(FormError(key, "error.invalid")))
        }

      override def unbind(key: String, value: Boolean): Map[String, String] =
        Map(key -> value.toString)
    }

  private val emptyForm: Form[Boolean] =
    Form("value" -> Forms.of[Boolean])

  def get: Action[AnyContent]          = Action.async { implicit request =>
    val existingValue = getFromSession[Boolean]("yesNo")
    val form          = existingValue.map(emptyForm.fill).getOrElse(emptyForm)

    renderer
      .render(
        "yesNo.njk",
        Json.obj(
          "form"   -> form,
          "radios" -> Radios.yesNo(form("value"))
        )
      )
      .map(Ok(_))
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    emptyForm
      .bindFromRequest()
      .fold(
        errors =>
          renderer
            .render(
              "yesNo.njk",
              Json.obj(
                "form"   -> errors,
                "radios" -> Radios.yesNo(errors("value"))
              )
            )
            .map(BadRequest(_)),
        value =>
          Future.successful {
            Redirect(controllers.routes.YesNoController.get)
              .addingToSession("yesNo" -> Json.stringify(Json.toJson(value)))
          }
      )
  }
}
