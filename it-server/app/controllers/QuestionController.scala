package controllers

import javax.inject.{Inject, Singleton}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.viewmodels.{Fieldset, Legend, TextContent}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class QuestionController @Inject() (
                                     cc: ControllerComponents
                                   )(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  def ok: Action[AnyContent] = Action {
    implicit request =>

      val fieldset = Fieldset(Some(Legend(TextContent("my legend"))))

      Ok("ok")
  }
}
