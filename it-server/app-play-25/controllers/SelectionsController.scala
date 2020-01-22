package controllers

import com.google.inject.Inject
import play.api.data.format.Formatter
import play.api.data.{Form, FormError, Forms}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import uk.gov.hmrc.nunjucks.NunjucksRenderer
import uk.gov.hmrc.viewmodels.{Selections, _}

import scala.concurrent.ExecutionContext

sealed trait SelectAnswer
object SelectAnswer {

  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  case object A extends SelectAnswer
  case object B extends SelectAnswer
  case object C extends SelectAnswer

  implicit lazy val writes: Writes[SelectAnswer] =
    Writes.of[String].contramap {
      case A => "A"
      case B => "B"
      case C => "C"
    }

  implicit lazy val reads: Reads[SelectAnswer] =
    Reads.of[String].flatMap {
      case "A" => Reads.pure(A)
      case "B" => Reads.pure(B)
      case "C" => Reads.pure(C)
      case _   => Reads(_ => JsError("error.invalid"))
    }

  implicit val binding: Formatter[SelectAnswer] = new Formatter[SelectAnswer] {

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], SelectAnswer] =
      data.get(key) match {
        case Some("A") => Right(A)
        case Some("B") => Right(B)
        case Some("C") => Right(C)
        case Some("")  => Left(Seq(FormError(key, "error.required")))
        case _         => Left(Seq(FormError(key, "error.invalid")))
      }

    override def unbind(key: String, value: SelectAnswer): Map[String, String] =
      Map(key -> value.toString)
  }
}



class SelectionsController @Inject()(
                                      renderer: NunjucksRenderer
                                    )(implicit ec: ExecutionContext, override val messagesApi: MessagesApi) extends Controller with I18nSupport {


  private def selectOptions(form: Form[_])(implicit messages: Messages): Seq[Selections.Item] = {

    val field = form("value")
    val items = Seq(
      Selections.Select(msg"selectOptions.a", "published"),
      Selections.Select(msg"selectOptions.b", "updated"),
      Selections.Select(msg"selectOptions.c", "views"),
      Selections.Select(msg"selectOptions.d", "comments")

    )

    Selections.set(field, items)
  }
  private val emptyForm = Form(
    "value" -> Forms.set(Forms.of[SelectAnswer])
      .verifying("error.required", _.nonEmpty)
  )
  def get: Action[AnyContent] = Action.async {
    implicit request =>

      val existingValue = getFromSession[Set[SelectAnswer]]("select")
      val form = existingValue.map(emptyForm.fill).getOrElse(emptyForm)

      renderer.render("select.njk", Json.obj(
        "form"       -> form,
        "items" -> selectOptions(form))).map(Ok(_))
  }

}