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
