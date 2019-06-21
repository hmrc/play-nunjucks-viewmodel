package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.libs.json.Json

class LegendSpec extends FreeSpec with MustMatchers with OptionValues {

  "Legend" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val content = TextContent("foo")
        val classes = Some("bar")
        val isPageHeader = true

        val legend = Legend(content, classes, isPageHeader)

        Json.toJson(legend) mustEqual Json.obj(
          "text" -> "foo",
          "classes" -> "bar",
          "isPageHeading" -> true
        )
      }

      "when no optional values are supplied" in {

        val legend = Legend(TextContent("foo"))

        Json.toJson(legend) mustEqual Json.obj(
          "text" -> "foo",
          "isPageHeading" -> false
        )
      }
    }
  }
}
