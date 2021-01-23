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

package uk.gov.hmrc.mappings

import java.time.LocalDate

import uk.gov.hmrc.viewmodels._
import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.data.{Form, FormError}

class LocalDateMappingSpec extends FreeSpec with MustMatchers with OptionValues {

  val form = Form(
    "date" -> new LocalDateMapping()
  )

  "a LocalDateFormatter" - {

    "must bind a valid date" in {

      val result = form.bind(Map(
        "date.day"   -> "1",
        "date.month" -> "2",
        "date.year"  -> "2001"
      ))

      result.value.value mustEqual LocalDate.of(2001, 2, 1)
    }

    "must fail to bind an invalid date" in {

      val result = form.bind(Map(
        "date.day"   -> "99",
        "date.month" -> "2",
        "date.year"  -> "2001"
      ))

      result.errors must contain only (FormError("date", Seq("date.invalid")))
    }

    "must fail to bind a date with all empty fields" in {

      val result = form.bind(Map(
        "date.day"   -> "",
        "date.month" -> "",
        "date.year"  -> ""
      ))

      result.errors must contain only (FormError("date", Seq("date.required")))
    }

    "must fail to bind a date with all missing fields" in {

      val result = form.bind(Map.empty[String, String])

      result.errors must contain only (FormError("date", Seq("date.required")))
    }

    "must fail to bind a date with a single missing" - {

      "day field" in {

        val result = form.bind(Map(
          "date.month" -> "1",
          "date.year"  -> "2001"
        ))

        result.errors must contain only (FormError("date", Seq("date.required.1"), Seq(msg"site.day")))
      }

      "month field" in {

        val result = form.bind(Map(
          "date.day" -> "1",
          "date.year"  -> "2001"
        ))

        result.errors must contain only (FormError("date.month", Seq("date.required.1"), Seq(msg"site.month")))
      }

      "year field" in {

        val result = form.bind(Map(
          "date.day"   -> "1",
          "date.month" -> "2"
        ))

        result.errors must contain only (FormError("date.year", Seq("date.required.1"), Seq(msg"site.year")))
      }
    }

    "must fail to bind with missing" - {

      "day and month fields" in {

        val result = form.bind(Map(
          "date.year" -> "2001"
        ))

        result.errors must contain only (FormError("date", Seq("date.required.2"), Seq(msg"site.day", msg"site.month")))
      }

      "day and year fields" in {

        val result = form.bind(Map(
          "date.month" -> "1"
        ))

        result.errors must contain only (FormError("date", Seq("date.required.2"), Seq(msg"site.day", msg"site.year")))
      }

      "month and year fields" in {

        val result = form.bind(Map(
          "date.day" -> "1"
        ))

        result.errors must contain only (FormError("date.month", Seq("date.required.2"), Seq(msg"site.month", msg"site.year")))
      }
    }

    "must fail to bind with invalid" - {

      "day field" in {

        val result = form.bind(Map(
          "date.day"   -> "foo",
          "date.month" -> "1",
          "date.year"  -> "2001"
        ))

        result.errors must contain only (FormError("date", Seq("date.invalid")))
      }

      "month field" in {

        val result = form.bind(Map(
          "date.day"   -> "1",
          "date.month" -> "foo",
          "date.year"  -> "2001"
        ))

        result.errors must contain only (FormError("date", Seq("date.invalid")))
      }

      "year field" in {

        val result = form.bind(Map(
          "date.day"   -> "1",
          "date.month" -> "2",
          "date.year"  -> "foo"
        ))

        result.errors must contain only (FormError("date", Seq("date.invalid")))
      }

      "day and month fields" in {

        val result = form.bind(Map(
          "date.day"   -> "foo",
          "date.month" -> "foo",
          "date.year"  -> "2001"
        ))

        result.errors.length mustEqual 1
        result.errors must contain only (FormError("date", Seq("date.invalid")))
      }

      "day and year fields" in {

        val result = form.bind(Map(
          "date.day"   -> "foo",
          "date.month" -> "1",
          "date.year"  -> "foo"
        ))

        result.errors.length mustEqual 1
        result.errors must contain only (FormError("date", Seq("date.invalid")))
      }

      "month and year fields" in {

        val result = form.bind(Map(
          "date.day"   -> "1",
          "date.month" -> "foo",
          "date.year"  -> "foo"
        ))

        result.errors.length mustEqual 1
        result.errors must contain only (FormError("date", Seq("date.invalid")))
      }
    }

    "must unbind a valid date" in {

      val result = form.fill(LocalDate.of(2001, 2, 1))

      result.data must contain only (
        "date.day"   -> "1",
        "date.month" -> "2",
        "date.year"  -> "2001"
      )
    }
  }
}
