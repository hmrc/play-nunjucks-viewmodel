package controllers

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import forms.DateOfBirthFormProvider
import javax.inject.Inject
import play.api.data.{Form, Forms}
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._
import uk.gov.hmrc.nunjucks.{NunjucksRenderer, NunjucksSupport}
import viewmodels.{DateOfBirthPageViewModel, LivesInUKPageViewModel, LocationPageViewModel, TextboxPageViewModel}

import scala.concurrent.{ExecutionContext, Future}

class SingleComponentController @Inject() (
                                            cc: ControllerComponents,
                                            val renderer: NunjucksRenderer
                                          )(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with NunjucksSupport {

  private val form: Form[String] = Form(
    "value" -> Forms.text.verifying("error.required", _.nonEmpty)
    )

  def getLocation: Action[AnyContent] = Action.async {
    implicit request =>

      val boundForm = request.session.get("location")
        .map(form.fill)
        .getOrElse(form)

      val viewModel = LocationPageViewModel(boundForm)

      renderer.render("location.njk", viewModel).map(Ok(_))
  }

  def submitLocation: Action[AnyContent] = Action.async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {

          val viewModel = LocationPageViewModel(formWithErrors)
          renderer.render("location.njk", viewModel).map(BadRequest(_))
        },

        value => {
          Future.successful(
            Redirect(controllers.routes.SingleComponentController.getLocation())
              .addingToSession("location" -> value)
          )
        }
      )
  }

  def getName: Action[AnyContent] = Action.async {
    implicit request =>

      val boundForm = request.session.get("name")
        .map(form.fill)
        .getOrElse(form)

      val viewModel = TextboxPageViewModel(boundForm)

      renderer.render("name.njk", viewModel).map(Ok(_))
  }

  def submitName: Action[AnyContent] = Action.async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {

          val viewModel = TextboxPageViewModel(formWithErrors)
          renderer.render("name.njk", viewModel).map(BadRequest(_))
        },

        value => {
          Future.successful(
            Redirect(controllers.routes.SingleComponentController.getName())
              .addingToSession("name" -> value)
          )
        }
      )
  }

  def getDateOfBirth: Action[AnyContent] = Action.async {
    implicit request =>

      val form = new DateOfBirthFormProvider()()

      val boundForm =
        request.session.get("dateOfBirth").map(LocalDate.parse(_))
        .map(form.fill)
        .getOrElse(form)

      val viewModel = DateOfBirthPageViewModel(boundForm)

      renderer.render("dateOfBirth.njk", viewModel).map(Ok(_))
  }

  def submitDateOfBirth: Action[AnyContent] = Action.async {
    implicit request =>

      val form = new DateOfBirthFormProvider()()

      form.bindFromRequest().fold(
        formWithErrors => {

          val viewModel = DateOfBirthPageViewModel(formWithErrors)
          renderer.render("dateOfBirth.njk", viewModel).map(BadRequest(_))
        },

        value => {
          Future.successful(
            Redirect(controllers.routes.SingleComponentController.getDateOfBirth())
              .addingToSession("dateOfBirth" -> value.format(dateFormatter))
          )
        }
      )
  }

  def getLivesInUK: Action[AnyContent] = Action.async {
    implicit request =>

      val boundForm = request.session.get("livesInUK")
        .map(form.fill)
        .getOrElse(form)

      val viewModel = LivesInUKPageViewModel(boundForm)

      renderer.render("livesInUK.njk", viewModel).map(Ok(_))
  }

  def submitLivesInUK: Action[AnyContent] = Action.async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors => {

          val viewModel = LivesInUKPageViewModel(formWithErrors)
          renderer.render("livesInUK.njk", viewModel).map(BadRequest(_))
        },

        value => {
          Future.successful(
            Redirect(controllers.routes.SingleComponentController.getLivesInUK())
              .addingToSession("livesInUk" -> value)
          )
        }
      )
  }

  private val dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")
}
