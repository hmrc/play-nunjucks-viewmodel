package controllers

import com.google.inject.Inject
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.nunjucks.NunjucksRenderer
import uk.gov.hmrc.viewmodels._

import scala.concurrent.ExecutionContext

class TableController @Inject()(
                                       renderer: NunjucksRenderer,
                                       cc: ControllerComponents
                                     )(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  def get: Action[AnyContent] = Action.async {
    implicit request =>

      val table: Table = {

        val head = Seq(
          Cell(msg"table.header.c1", classes = Seq("govuk-!-width-one-quarter")),
          Cell(msg"table.header.c2", classes = Seq("govuk-!-width-one-quarter")),
          Cell(msg"table.header.c3", classes = Seq("govuk-!-width-one-quarter")),
          Cell(msg"table.header.c4", classes = Seq("govuk-!-width-one-quarter")))

        val rows = members.map { data =>
          Seq(
            Cell(msg"table.row1.col1", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row1.col2", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row1.col3", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row1.col4", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row2.col1", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row2.col2", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row2.col3", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row2.col4", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row3.col1", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row3.col2", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row3.col3", classes = Seq("govuk-!-width-one-quarter")),
            Cell(msg"table.row3.col4", classes = Seq("govuk-!-width-one-quarter"))
          )

        Table(head = head, rows = rows, caption = Some(msg"table.caption"), firstCellIsHeader = true)
      }

      renderer.render("table.njk", Json.obj(
        "table" -> table
      )).map(Ok(_))
  }
}
