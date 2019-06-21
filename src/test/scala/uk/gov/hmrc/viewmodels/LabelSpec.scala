package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.libs.json.Json

class LabelSpec extends FreeSpec with MustMatchers {

  "Label" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val content = TextContent("foo")
        val classes = Some("bar")
        val attributes = Map("key1" -> "value1")
        val forId = "id"
        val isPageHeading = true

        val label = Label(forId, content, isPageHeading, classes, attributes)

        Json.toJson(label) mustEqual Json.obj(
          "text"          -> "foo",
          "for"           -> "id",
          "isPageHeading" -> true,
          "classes"       -> "bar",
          "attributes"    -> Json.obj("key1" -> "value1")
        )
      }

      "when no optional values are supplied" in {

        val content = TextContent("foo")
        val forId = "id"
        val label = Label(forId, content)

        Json.toJson(label) mustEqual Json.obj(
          "text"          -> "foo",
          "for"           -> "id",
          "isPageHeading" -> false
        )
      }
    }
  }
}
