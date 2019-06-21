package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.libs.json.Json

class HintSpec extends FreeSpec with MustMatchers {

  "Hint" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val content = TextContent("foo")
        val classes = Some("bar")

        val attributes = Map("key1" -> "value1")
        val id = Some("id")

        val hint = Hint(content, id, classes, attributes)

        Json.toJson(hint) mustEqual Json.obj(
          "text"       -> "foo",
          "id"         -> "id",
          "classes"    -> "bar",
          "attributes" -> Json.obj("key1" -> "value1")
        )
      }

      "when no optional values are supplied" in {

        val content = TextContent("foo")
        val hint = Hint(content)

        Json.toJson(hint) mustEqual Json.obj("text" -> "foo")
      }
    }
  }
}
