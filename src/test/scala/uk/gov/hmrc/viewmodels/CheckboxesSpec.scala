package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.libs.json.Json
import play.twirl.api.Html

class CheckboxesSpec extends FreeSpec with MustMatchers {

  "Checkboxes" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val itemContent = TextContent("foo")
        val value = "bar"
        val item = CheckboxItem(itemContent, value)

        val name = "name"
        val describedBy = Some("described by")
        val legend = Some(Legend(TextContent("legend")))
        val fieldset = Some(Fieldset(legend))
        val hint = Some(Hint(TextContent("hint")))
        val formGroupClasses = Some("form group classes")
        val idPrefix = Some("id prefix")
        val classes = Some("classes")
        val attributes = Map("key1" -> "value1")

        val checkboxes = Checkboxes(name, Seq(item), describedBy, fieldset, hint, formGroupClasses, idPrefix, classes, attributes)

        Json.toJson(checkboxes) mustEqual Json.obj(
          "name"        -> "name",
          "items"       -> Json.arr(
            Json.obj(
              "text"     -> "foo",
              "value"    -> "bar",
              "checked"  -> false,
              "disabled" -> false
            )
          ),
          "describedBy" -> "described by",
          "fieldset"    -> Json.obj(
            "legend" -> Json.obj(
              "text"          -> "legend",
              "isPageHeading" -> false
            )
          ),
          "hint"             -> Json.obj("text" -> "hint"),
          "formGroupClasses" -> "form group classes",
          "idPrefix"         -> "id prefix",
          "classes"          -> "classes",
          "attributes"       -> Json.obj("key1" -> "value1")
        )
      }

      "when no optional values are supplied" in {

        val itemContent = TextContent("foo")
        val value = "bar"
        val item = CheckboxItem(itemContent, value)

        val name = "name"

        val checkboxes = Checkboxes(name, Seq(item))

        Json.toJson(checkboxes) mustEqual Json.obj(
          "name"        -> "name",
          "items"       -> Json.arr(
            Json.obj(
              "text"     -> "foo",
              "value"    -> "bar",
              "checked"  -> false,
              "disabled" -> false
            )
          )
        )
      }
    }

    "must throw an exception" - {

      "when constructed with an empty sequence of items" in {

        an[IllegalArgumentException] must be thrownBy Checkboxes("name", Seq.empty)
      }
    }
  }
}
