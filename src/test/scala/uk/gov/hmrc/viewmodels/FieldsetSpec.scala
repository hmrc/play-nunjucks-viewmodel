package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.libs.json.Json

class FieldsetSpec extends FreeSpec with MustMatchers with OptionValues {

  "Fieldset" - {

    "must serialise" - {

      "when all values are supplied" in {

        val content = TextContent("foo")
        val classes = Some("bar")
        val isPageHeader = true

        val legend = Legend(content, classes, isPageHeader)

        val fieldset = Fieldset(Some(legend), Some("foo"), Some("bar"), Map("key1" -> "value1"), Some("baz"))

        Json.toJson(fieldset) mustEqual Json.obj(
          "legend"      -> Json.obj("text"          -> "foo",
                                    "classes"       -> "bar",
                                    "isPageHeading" -> true),
          "describedBy" -> "foo",
          "classes"     -> "bar",
          "attributes"  -> Json.obj("key1" -> "value1"),
          "called"      -> "baz"
        )
      }

      "when all values are not supplied" in {

        val fieldset = Fieldset()

        Json.toJson(fieldset) mustEqual Json.obj()
      }
    }
  }
}
