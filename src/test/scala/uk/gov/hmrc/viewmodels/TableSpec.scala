/*
 * Copyright 2021 HM Revenue & Customs
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

package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.i18n.Messages
import play.api.libs.json.{JsArray, Json}
import uk.gov.hmrc.viewmodels.Table.Cell
import uk.gov.hmrc.viewmodels.Text.Literal
import play.api.test.Helpers

class TableSpec extends FreeSpec with MustMatchers with OptionValues {

  implicit val messages: Messages = Helpers.stubMessages()

  "Table Cell" - {

    "must write with text content" in {

      val cell = Table.Cell(lit"foo")

      val expectedJson = Json.obj(
        "text" -> "foo"
      )

      Json.toJson(cell) mustEqual expectedJson
    }

    "must write with html content" in {

      val cell = Table.Cell(Html("<b>foo</b>"))

      val expectedJson = Json.obj(
        "html" -> "<b>foo</b>"
      )

      Json.toJson(cell) mustEqual expectedJson
    }

    "must write with classes" in {

      val cell = Table.Cell(lit"foo", classes = Seq("bar", "baz"))

      val expectedJson = Json.obj(
        "text"    -> "foo",
        "classes" -> "bar baz"
      )

      Json.toJson(cell) mustEqual expectedJson
    }

    "must write with format" in {

      val cell = Table.Cell(lit"foo", Seq.empty, Some("test-format"))

      val expectedJson = Json.obj(
        "text" -> "foo",
        "format" -> "test-format"
      )

      Json.toJson(cell) mustEqual expectedJson
    }

    "must write with attributes" in {

      val cell = Table.Cell(lit"foo")
        .copy(attributes = Map("key" -> "value"))

      val expectedJson = Json.obj(
        "text" -> "foo",
        "attributes" -> Json.obj(
          "key" -> "value"
        )
      )

      Json.toJson(cell) mustEqual expectedJson
    }
  }

  "Table" - {

    "must write with caption" in {

      val table = Table(caption = Some(Literal("test-caption")), head = Seq.empty, rows = tableRows)

      val expectedJson = Json.obj(
        "caption" -> "test-caption",
        "firstCellIsHeader" -> false,
        "rows" -> expectedTableRowsJson
      )

      Json.toJson(table) mustEqual expectedJson
    }

    "must write with caption classes" in {

      val table = Table(caption = Some(Literal("test-caption")), captionClasses = Seq("bar", "baz"),
        head = Seq.empty, rows = tableRows)

      val expectedJson = Json.obj(
        "caption" -> "test-caption",
        "captionClasses" -> "bar baz",
        "firstCellIsHeader" -> false,
        "rows" -> expectedTableRowsJson
      )

      Json.toJson(table) mustEqual expectedJson
    }

    "must write with firstCellIsHeader property set" in {

      val table = Table(firstCellIsHeader = true, head = Seq.empty, rows = tableRows)

      val expectedJson = Json.obj(
        "firstCellIsHeader" -> true,
        "rows" -> expectedTableRowsJson
      )

      Json.toJson(table) mustEqual expectedJson
    }

    "must write header" in {

      val header = Seq(Cell(lit"foo-head"), Cell(lit"bar-head"))
      val table = Table(head = header, rows = tableRows)

      val expectedJson = Json.obj(
        "firstCellIsHeader" -> false,
        "head" -> Json.arr(
          Json.obj("text" -> "foo-head"),
          Json.obj("text" -> "bar-head")
        ),
        "rows" -> expectedTableRowsJson
      )

      Json.toJson(table) mustEqual expectedJson
    }

    "must write rows" in {

      val table = Table(head = Seq.empty, rows = tableRows)

      val expectedJson = Json.obj(
        "firstCellIsHeader" -> false,
        "rows" -> expectedTableRowsJson
      )

      Json.toJson(table) mustEqual expectedJson
    }

    "must write with classes" in {

      val table = Table(head = Seq.empty, rows = tableRows, classes = Seq("bar", "baz"))

      val expectedJson = Json.obj(
        "firstCellIsHeader" -> false,
        "rows" -> expectedTableRowsJson,
        "classes" -> "bar baz"
      )

      Json.toJson(table) mustEqual expectedJson
    }

    "must write with attributes" in {

      val cell = Table.Cell(lit"foo")
        .copy(attributes = Map("key" -> "value"))

      val expectedJson = Json.obj(
        "text" -> "foo",
        "attributes" -> Json.obj(
          "key" -> "value"
        )
      )

      Json.toJson(cell) mustEqual expectedJson
    }
  }

  val tableRows: Seq[Seq[Cell]] = Seq(
    Seq(Cell(lit"foo"), Cell(lit"bar")),
    Seq(Cell(lit"foo1"), Cell(lit"bar1")),
    Seq(Cell(lit"foo2"), Cell(lit"bar2"))
  )

  val expectedTableRowsJson: JsArray = Json.arr(
    Json.arr(
      Json.obj("text" -> "foo"),
      Json.obj("text" -> "bar")
    ),
    Json.arr(
      Json.obj("text" -> "foo1"),
      Json.obj("text" -> "bar1")
    ),
    Json.arr(
      Json.obj("text" -> "foo2"),
      Json.obj("text" -> "bar2")
    )
  )
}
