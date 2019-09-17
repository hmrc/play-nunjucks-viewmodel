package controllers

import com.google.inject.Inject
import play.api.data.{Form, FormError, Forms}
import play.api.data.format.Formatter
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.nunjucks.NunjucksRenderer

import scala.concurrent.{ExecutionContext, Future}

sealed trait RadioAnswer
object RadioAnswer {

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  case object A extends RadioAnswer
  case object B extends RadioAnswer
  case object C extends RadioAnswer

  implicit lazy val writes: Writes[RadioAnswer] =
    Writes.of[String].contramap {
      case A => "A"
      case B => "B"
      case C => "C"
    }

  implicit lazy val reads: Reads[RadioAnswer] =
    Reads.of[String].flatMap {
      case "A" => Reads.pure(A)
      case "B" => Reads.pure(B)
      case "C" => Reads.pure(C)
      case _   => Reads(_ => JsError("error.invalid"))
    }

  implicit val binding: Formatter[RadioAnswer] = new Formatter[RadioAnswer] {

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], RadioAnswer] =
      data.get(key) match {
        case Some("A") => Right(A)
        case Some("B") => Right(B)
        case Some("C") => Right(C)
        case Some("")  => Left(Seq(FormError(key, "error.required")))
        case _         => Left(Seq(FormError(key, "error.invalid")))
      }

    override def unbind(key: String, value: RadioAnswer): Map[String, String] =
      Map(key -> value.toString)
  }
}

class RadiosController @Inject()(
                                  renderer: NunjucksRenderer,
                                  cc: ControllerComponents
                                )(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  import uk.gov.hmrc.viewmodels._

  private def radios(form: Form[_])(implicit messages: Messages): Seq[Radios.Item] = {

    val field = form("value")
    val items = Seq(
      Radios.Radio(Messages("radios.a"), "A"),
      Radios.Radio(Messages("radios.b"), "B"),
      Radios.Radio(Messages("radios.c"), "C")
    )

    Radios(field, items)
  }

  private val emptyForm: Form[RadioAnswer] =
    Form("value" -> Forms.of[RadioAnswer])

  def get: Action[AnyContent] = Action.async {
    implicit request =>

      val existingValue = getFromSession[RadioAnswer]("radios")
      val form = existingValue.map(emptyForm.fill).getOrElse(emptyForm)

      renderer.render("radios.njk", Json.obj(
        "form"   -> form,
        "radios" -> radios(form)
      )).map(Ok(_))
  }

  def post: Action[AnyContent] = Action.async {
    implicit request =>

      emptyForm.bindFromRequest().fold(
        errors =>
          renderer.render("radios.njk", Json.obj(
            "form"   -> errors,
            "radios" -> radios(errors)
          )).map(BadRequest(_)),
        value =>
          Future.successful {
            Redirect(controllers.routes.RadiosController.get())
              .addingToSession("radios" -> Json.stringify(Json.toJson(value)))
          }
      )
  }
}
