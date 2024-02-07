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

package uk.gov.hmrc.viewmodels

import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.test.Helpers

class SummaryListSpec extends AnyFreeSpec with Matchers with OptionValues {

  implicit val messages: Messages = Helpers.stubMessages()

  "summary list key" - {

    "must write with text content" in {

      val key = SummaryList.Key(lit"foo")

      val expectedJson = Json.obj(
        "text" -> "foo"
      )

      Json.toJson(key) mustEqual expectedJson
    }

    "must write with html content" in {

      val key = SummaryList.Key(Html("<b>foo</b>"))

      val expectedJson = Json.obj(
        "html" -> "<b>foo</b>"
      )

      Json.toJson(key) mustEqual expectedJson
    }

    "must write with classes" in {

      val key = SummaryList.Key(lit"foo", classes = Seq("bar", "baz"))

      val expectedJson = Json.obj(
        "text"    -> "foo",
        "classes" -> "bar baz"
      )

      Json.toJson(key) mustEqual expectedJson
    }
  }

  "summary list value" - {

    "must write with text content" in {

      val value = SummaryList.Value(lit"foo")

      val expectedJson = Json.obj(
        "text" -> "foo"
      )

      Json.toJson(value) mustEqual expectedJson
    }

    "must write with html content" in {

      val value = SummaryList.Value(Html("<b>foo</b>"))

      val expectedJson = Json.obj(
        "html" -> "<b>foo</b>"
      )

      Json.toJson(value) mustEqual expectedJson
    }

    "must write with classes" in {

      val value = SummaryList.Value(lit"foo", classes = Seq("bar", "baz"))

      val expectedJson = Json.obj(
        "text"    -> "foo",
        "classes" -> "bar baz"
      )

      Json.toJson(value) mustEqual expectedJson
    }
  }

  "summary list action" - {

    "must write with text content" in {

      val action = SummaryList.Action(lit"foo", "bar")

      val expectedJson = Json.obj(
        "text" -> "foo",
        "href" -> "bar"
      )

      Json.toJson(action) mustEqual expectedJson
    }

    "must write with html content" in {

      val action = SummaryList.Action(Html("<b>foo</b>"), "baz")

      val expectedJson = Json.obj(
        "html" -> "<b>foo</b>",
        "href" -> "baz"
      )

      Json.toJson(action) mustEqual expectedJson
    }

    "must write with classes" in {

      val action = SummaryList
        .Action(lit"foo", "bar")
        .copy(classes = Seq("spoon", "fork"))

      val expectedJson = Json.obj(
        "text"    -> "foo",
        "href"    -> "bar",
        "classes" -> "spoon fork"
      )

      Json.toJson(action) mustEqual expectedJson
    }

    "must write with visually hidden text" in {

      val action = SummaryList
        .Action(lit"foo", "bar")
        .copy(visuallyHiddenText = Some(lit"baz"))

      val expectedJson = Json.obj(
        "text"               -> "foo",
        "href"               -> "bar",
        "visuallyHiddenText" -> "baz"
      )

      Json.toJson(action) mustEqual expectedJson
    }

    "must write with attributes" in {

      val action       = SummaryList
        .Action(lit"foo", "bar")
        .copy(attributes = Map("key" -> "value"))

      val expectedJson = Json.obj(
        "text"       -> "foo",
        "href"       -> "bar",
        "attributes" -> Json.obj(
          "key" -> "value"
        )
      )

      Json.toJson(action) mustEqual expectedJson
    }
  }

  "summary list row" - {

    "must write with an action" in {

      val key    = SummaryList.Key(lit"foo")
      val value  = SummaryList.Value(lit"bar")
      val action = SummaryList.Action(lit"baz", "quux")

      val row = SummaryList.Row(
        key = key,
        value = value,
        actions = List(
          action
        )
      )

      val expectedJson = Json.obj(
        "key"     -> key,
        "value"   -> value,
        "actions" -> Json.obj(
          "items" -> Json.arr(
            action
          )
        )
      )

      Json.toJson(row) mustEqual expectedJson
    }

    "must write without actions" in {

      val key   = SummaryList.Key(lit"foo")
      val value = SummaryList.Value(lit"bar")

      val row = SummaryList.Row(
        key = key,
        value = value
      )

      val expectedJson = Json.obj(
        "key"   -> key,
        "value" -> value
      )

      Json.toJson(row) mustEqual expectedJson
    }
  }
}
