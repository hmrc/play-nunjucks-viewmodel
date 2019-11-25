package controllers

import com.google.inject.Inject
import play.api.data.format.Formatter
import play.api.data.{Form, FormError, Forms}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import uk.gov.hmrc.nunjucks.NunjucksRenderer

import scala.concurrent.{ExecutionContext, Future}

class YesNoController @Inject()(
                                 renderer: NunjucksRenderer
                               )(implicit ec: ExecutionContext, override val messagesApi: MessagesApi) extends Controller with I18nSupport {

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

  def get: Action[AnyContent] = Action.async {
    implicit request =>

      val existingValue = getFromSession[Boolean]("yesNo")
      val form = existingValue.map(emptyForm.fill).getOrElse(emptyForm)

      renderer.render("yesNo.njk", Json.obj(
        "form"   -> form,
        "radios" -> Radios.yesNo(form("value"))
      )).map(Ok(_))
  }

  def post: Action[AnyContent] = Action.async {
    implicit request =>

      emptyForm.bindFromRequest().fold(
        errors =>
          renderer.render("yesNo.njk", Json.obj(
            "form"   -> errors,
            "radios" -> Radios.yesNo(errors("value"))
          )).map(BadRequest(_)),
        value =>
          Future.successful {
            Redirect(controllers.routes.YesNoController.get())
              .addingToSession("yesNo" -> Json.stringify(Json.toJson(value)))
          }
      )
  }
}
