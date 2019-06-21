package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.libs.json.Json
import play.twirl.api.Html

class CheckboxItemSpec extends FreeSpec with MustMatchers {

  "Checkbox Item" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val content = TextContent("foo")
        val value = "value"
        val id = Some("id")
        val name = Some("name")
        val hint = Some(Hint(TextContent("hint")))
        val checked = true
        val disabled = true
        val conditionalHtml = Some(HtmlContent(Html("<i>foo</i>")))
        val labelClasses = Some("bar")
        val attributes = Map("key1" -> "value1")
        val labelAttributes = Map("key2" -> "value2")

        val item = CheckboxItem(content, value, id, name, hint, checked, disabled, conditionalHtml, attributes, labelClasses, labelAttributes)

        Json.toJson(item) mustEqual Json.obj(
          "text"        -> "foo",
          "value"       -> "value",
          "id"          -> "id",
          "name"        -> "name",
          "hint"        -> Json.obj("text" -> "hint"),
          "checked"     -> true,
          "disabled"    -> true,
          "conditional" -> Json.obj("html" -> "<i>foo</i>"),
          "label"       -> Json.obj(
            "classes"     -> "bar",
            "attributes"  -> Json.obj("key2" -> "value2")
          ),
          "attributes"  -> Json.obj("key1" -> "value1")
        )
      }

      "when no optional values are supplied" in {

        val content = TextContent("foo")
        val value = "bar"
        val item = CheckboxItem(content, value)

        Json.toJson(item) mustEqual Json.obj(
          "text"     -> "foo",
          "value"    -> "bar",
          "checked"  -> false,
          "disabled" -> false
        )
      }
    }
  }
}
