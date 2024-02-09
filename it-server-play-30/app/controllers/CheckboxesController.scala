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
import play.api.data._
import play.api.data.format.Formatter
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._
import uk.gov.hmrc.nunjucks.NunjucksRenderer
import uk.gov.hmrc.viewmodels._

import scala.concurrent.{ExecutionContext, Future}

sealed trait CheckboxAnswer
case object A extends CheckboxAnswer
case object B extends CheckboxAnswer
case object C extends CheckboxAnswer

object CheckboxAnswer {

  import play.api.libs.json._

  implicit val writes: Writes[CheckboxAnswer] =
    Writes.of[String].contramap(_.toString)

  implicit val reads: Reads[CheckboxAnswer] = {
    Reads.of[String].flatMap {
      case "A" => Reads.pure(A)
      case "B" => Reads.pure(B)
      case "C" => Reads.pure(C)
      case _   => Reads(_ => JsError("error.invalid"))
    }
  }

  implicit val binding: Formatter[CheckboxAnswer] = new Formatter[CheckboxAnswer] {

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], CheckboxAnswer] =
      data.get(key) match {
        case Some("A") => Right(A)
        case Some("B") => Right(B)
        case Some("C") => Right(C)
        case _         => Left(Seq(FormError(key, "error.invalid")))
      }

    override def unbind(key: String, value: CheckboxAnswer): Map[String, String] =
      Map(key -> value.toString)
  }
}

class CheckboxesController @Inject()(
                                      renderer: NunjucksRenderer,
                                      cc: ControllerComponents
                                    )(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  private def checkboxes(form: Form[_]): Seq[Checkboxes.Item] = {

    val field = form("value")
    val items = Seq(
      Checkboxes.Checkbox(msg"checkboxes.a", "A"),
      Checkboxes.Checkbox(msg"checkboxes.b", "B"),
      Checkboxes.Checkbox(msg"checkboxes.c", "C")
    )

    Checkboxes.set(field, items)
  }

  private val emptyForm = Form(
    "value" -> Forms.set(Forms.of[CheckboxAnswer])
      .verifying("error.required", _.nonEmpty)
  )

  def get: Action[AnyContent] = Action.async {
    implicit request =>

      val existingValue = getFromSession[Set[CheckboxAnswer]]("checkboxes")
      val form = existingValue.map(emptyForm.fill).getOrElse(emptyForm)

      renderer.render("checkboxes.njk", Json.obj(
        "form"       -> form,
        "checkboxes" -> checkboxes(form)
      )).map(Ok(_))
  }

  def post: Action[AnyContent] = Action.async {
    implicit request =>

      emptyForm.bindFromRequest().fold(
        errors =>
          renderer.render("checkboxes.njk", Json.obj(
            "form"       -> errors,
            "checkboxes" -> checkboxes(errors)
          )).map(BadRequest(_)),
        value =>
          Future.successful {
            Redirect(controllers.routes.CheckboxesController.get)
              .addingToSession("checkboxes" -> Json.stringify(Json.toJson(value)))
          }
      )
  }
}
