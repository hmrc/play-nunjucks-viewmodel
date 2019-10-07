package uk.gov.hmrc.mappings

import java.time.LocalDate

import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.data.{Form, FormError}
import uk.gov.hmrc.viewmodels.Message

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

        result.errors must contain only (FormError("date", Seq("date.required.1"), Seq(Message.Computed("site.day"))))
      }

      "month field" in {

        val result = form.bind(Map(
          "date.day" -> "1",
          "date.year"  -> "2001"
        ))

        result.errors must contain only (FormError("date.month", Seq("date.required.1"), Seq(Message.Computed("site.month"))))
      }

      "year field" in {

        val result = form.bind(Map(
          "date.day"   -> "1",
          "date.month" -> "2"
        ))

        result.errors must contain only (FormError("date.year", Seq("date.required.1"), Seq(Message.Computed("site.year"))))
      }
    }

    "must fail to bind with missing" - {

      "day and month fields" in {

        val result = form.bind(Map(
          "date.year" -> "2001"
        ))

        result.errors must contain only (FormError("date", Seq("date.required.2"), Seq(Message.Computed("site.day"), Message.Computed("site.month"))))
      }

      "day and year fields" in {

        val result = form.bind(Map(
          "date.month" -> "1"
        ))

        result.errors must contain only (FormError("date", Seq("date.required.2"), Seq(Message.Computed("site.day"), Message.Computed("site.year"))))
      }

      "month and year fields" in {

        val result = form.bind(Map(
          "date.day" -> "1"
        ))

        result.errors must contain only (FormError("date.month", Seq("date.required.2"), Seq(Message.Computed("site.month"), Message.Computed("site.year"))))
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
