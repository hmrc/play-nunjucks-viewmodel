package controllers

import com.google.inject.Inject
import play.api.data.{Form, Forms}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, Controller}
import uk.gov.hmrc.nunjucks.NunjucksRenderer
import uk.gov.hmrc.viewmodels.Checkboxes.Item
import uk.gov.hmrc.viewmodels._

import scala.concurrent.{ExecutionContext, Future}

final case class BooleanProductAnswer(
                                       a: Boolean,
                                       b: Boolean,
                                       c: Boolean
                                     )

object BooleanProductAnswer {
  implicit lazy val format: OFormat[BooleanProductAnswer] = Json.format
}

class BooleanProductController @Inject()(
                                      renderer: NunjucksRenderer
                                    )(implicit ec: ExecutionContext, override val messagesApi: MessagesApi) extends Controller with I18nSupport {

  private val emptyForm: Form[BooleanProductAnswer] = Form(
    "value" -> Forms.mapping(
      "a" -> Forms.boolean,
      "b" -> Forms.boolean,
      "c" -> Forms.boolean
    )(BooleanProductAnswer.apply)(BooleanProductAnswer.unapply)
      .verifying("error.required", answer => answer.a || answer.b || answer.c)
  )

  private def checkboxes(form: Form[_])(implicit messages: Messages): Seq[Item] = {

    val field = form("value")
    val items = Seq(
      Checkboxes.BooleanProduct(msg"booleanProduct.a", "a"),
      Checkboxes.BooleanProduct(msg"booleanProduct.b", "b"),
      Checkboxes.BooleanProduct(msg"booleanProduct.c", "c")
    )

    Checkboxes.booleanProduct(field, items)
  }

  def get: Action[AnyContent] = Action.async {
    implicit request =>

      val existingValue = getFromSession[BooleanProductAnswer]("booleanProduct")
      val form = existingValue.map(emptyForm.fill).getOrElse(emptyForm)

      renderer.render("booleanProduct.njk", Json.obj(
        "form"       -> form,
        "checkboxes" -> checkboxes(form)
      )).map(Ok(_))
  }

  def post: Action[AnyContent] = Action.async {
    implicit request =>

      emptyForm.bindFromRequest().fold(
        errors =>
          renderer.render("booleanProduct.njk", Json.obj(
            "form"       -> errors,
            "checkboxes" -> checkboxes(errors)
          )).map(BadRequest(_)),
        value =>
          Future.successful {
            Redirect(controllers.routes.BooleanProductController.get())
              .addingToSession("booleanProduct" -> Json.stringify(Json.toJson(value)))
          }
      )
  }
}
