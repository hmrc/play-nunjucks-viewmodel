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

import java.time.LocalDate

import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.data.validation.{Constraint, Invalid}
import play.api.data.{Form, Forms}
import play.api.i18n.{Messages, MessagesApi}
import play.api.libs.json.{JsArray, JsNull, Json}
import play.api.test.Helpers
import uk.gov.hmrc.mappings.LocalDateMapping

class FormWritesSpec extends FreeSpec with MustMatchers with OptionValues {

  implicit val messages: Messages = Helpers.stubMessages()

  "form writes" - {

    "must write a single field" - {

      "with no value" in {

        val form = Form(
          "foo" -> Forms.text
        )

        val expectedJson = Json.obj(
          "foo"    -> Json.obj(
            "value"  -> JsNull,
            "values" -> JsArray.empty
          ),
          "errors" -> JsArray.empty
        )

        val json = Json.toJson(form)

        json mustEqual expectedJson
      }

      "with a value" in {

        val form = Form(
          "foo" -> Forms.text
        ).fill("foobar")

        val expectedJson = Json.obj(
          "foo"    -> Json.obj(
            "value"  -> "foobar",
            "values" -> Json.arr("foobar")
          ),
          "errors" -> JsArray.empty
        )

        val json = Json.toJson(form)

        json mustEqual expectedJson
      }

      "with multiple values" in {

        val form = Form(
          "foo" -> Forms.seq(Forms.text)
        ).fill(List("bar", "baz"))

        val expectedJson = Json.obj(
          "foo"    -> Json.obj(
            "value"  -> JsNull,
            "values" -> Json.arr("bar", "baz")
          ),
          "errors" -> JsArray.empty
        )

        val json = Json.toJson(form)

        json mustEqual expectedJson
      }

      "with errors" in {

        val form         = Form(
          "foo" -> Forms.nonEmptyText
        ).bind(Map("foo" -> ""))

        val expectedJson = Json.obj(
          "foo"    -> Json.obj(
            "value"  -> "",
            "values" -> Json.arr(""),
            "error"  -> Json.obj(
              "text" -> "error.required"
            )
          ),
          "errors" -> Json.arr(
            Json.obj(
              "text" -> "error.required",
              "href" -> "#foo"
            )
          )
        )

        val json = Json.toJson(form)

        json mustEqual expectedJson
      }

      "with errors with arguments in their message" in {

        val messagesApi: MessagesApi = Helpers.stubMessagesApi(
          Map(
            "en" -> Map(
              "foo.invalid" -> "first arg: {0}, second arg: {1}"
            )
          )
        )

        implicit val messages: Messages = Helpers.stubMessages(messagesApi)

        val constraint: Constraint[String] = Constraint { _ =>
          Invalid("foo.invalid", "bar", 13)
        }

        val form         = Form(
          "foo" -> Forms.text
            .verifying(constraint)
        ).bind(Map("foo" -> "baz"))

        val expectedJson = Json.obj(
          "foo"    -> Json.obj(
            "value"  -> "baz",
            "values" -> Json.arr("baz"),
            "error"  -> Json.obj(
              "text" -> "first arg: bar, second arg: 13"
            )
          ),
          "errors" -> Json.arr(
            Json.obj(
              "text" -> "first arg: bar, second arg: 13",
              "href" -> "#foo"
            )
          )
        )

        val json = Json.toJson(form)

        json mustEqual expectedJson
      }
    }
  }

  "write nested fields" - {

    "with no value" in {

      val form = Form(
        "date" -> new LocalDateMapping()
      )

      val expectedJson = Json.obj(
        "date"   -> Json.obj(
          "value"  -> JsNull,
          "values" -> JsArray.empty,
          "day"    -> Json.obj(
            "value"  -> JsNull,
            "values" -> JsArray.empty
          ),
          "month"  -> Json.obj(
            "value"  -> JsNull,
            "values" -> JsArray.empty
          ),
          "year"   -> Json.obj(
            "value"  -> JsNull,
            "values" -> JsArray.empty
          )
        ),
        "errors" -> JsArray.empty
      )

      val json = Json.toJson(form)

      json mustEqual expectedJson
    }

    "with a value" in {

      val form = Form(
        "date" -> new LocalDateMapping()
      ).fill(LocalDate.of(2001, 2, 1))

      val expectedJson = Json.obj(
        "date"   -> Json.obj(
          "value"  -> JsNull,
          "values" -> JsArray.empty,
          "day"    -> Json.obj(
            "value"  -> "1",
            "values" -> Json.arr("1")
          ),
          "month"  -> Json.obj(
            "value"  -> "2",
            "values" -> Json.arr("2")
          ),
          "year"   -> Json.obj(
            "value"  -> "2001",
            "values" -> Json.arr("2001")
          )
        ),
        "errors" -> JsArray.empty
      )

      val json = Json.toJson(form)

      json mustEqual expectedJson
    }

    "with errors" in {

      val form = Form(
        "date" -> new LocalDateMapping()
      ).bind(Map.empty[String, String])

      val expectedJson = Json.obj(
        "date"   -> Json.obj(
          "value"  -> JsNull,
          "values" -> JsArray.empty,
          "error"  -> Json.obj(
            "text" -> "date.required"
          ),
          "day"    -> Json.obj(
            "value"  -> JsNull,
            "values" -> JsArray.empty
          ),
          "month"  -> Json.obj(
            "value"  -> JsNull,
            "values" -> JsArray.empty
          ),
          "year"   -> Json.obj(
            "value"  -> JsNull,
            "values" -> JsArray.empty
          )
        ),
        "errors" -> Json.arr(
          Json.obj(
            "text" -> "date.required",
            "href" -> "#date"
          )
        )
      )

      val json = Json.toJson(form)

      json mustEqual expectedJson
    }

    "with errors on a nested field" in {

      val form         = Form(
        "password" -> Forms.mapping(
          "first"  -> Forms.nonEmptyText,
          "second" -> Forms.nonEmptyText
        )(Tuple2.apply)(Tuple2.unapply)
      ).bind(Map("password.first" -> "foobar"))

      val expectedJson = Json.obj(
        "password" -> Json.obj(
          "value"  -> JsNull,
          "values" -> JsArray.empty,
          "first"  -> Json.obj(
            "value"  -> "foobar",
            "values" -> Json.arr("foobar")
          ),
          "second" -> Json.obj(
            "value"  -> JsNull,
            "values" -> JsArray.empty,
            "error"  -> Json.obj(
              "text" -> "error.required"
            )
          )
        ),
        "errors"   -> Json.arr(
          Json.obj(
            "text" -> "error.required",
            "href" -> "#password_second"
          )
        )
      )

      val json = Json.toJson(form)

      json mustEqual expectedJson
    }
  }
}
