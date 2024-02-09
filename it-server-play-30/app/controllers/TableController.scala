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
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.nunjucks.NunjucksRenderer
import uk.gov.hmrc.viewmodels._
import uk.gov.hmrc.viewmodels.Table.Cell

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

        val rows: Seq[Seq[Cell]] =
          Seq(
            Seq(
              Cell(msg"table.row1.col1", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row1.col2", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row1.col3", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row1.col4", classes = Seq("govuk-!-width-one-quarter"))
            ),
            Seq(
              Cell(msg"table.row2.col1", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row2.col2", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row2.col3", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row2.col4", classes = Seq("govuk-!-width-one-quarter"))
            ),
            Seq(
              Cell(msg"table.row3.col1", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row3.col2", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row3.col3", classes = Seq("govuk-!-width-one-quarter")),
              Cell(msg"table.row3.col4", classes = Seq("govuk-!-width-one-quarter"))
            )
          )

        Table(head = head, rows = rows, caption = Some(msg"table.caption"), firstCellIsHeader = true)
      }

      renderer.render("table.njk", Json.obj(
        "table" -> table
      )).map(Ok(_))
  }
}
