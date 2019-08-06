package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.data.{Form, Forms}
import play.api.i18n.{DefaultMessagesApi, Messages, MessagesApi, MessagesImpl}
import play.api.i18n.Lang.defaultLang
import play.api.libs.json.Json

class CheckboxesSpec extends FreeSpec with MustMatchers {

  implicit private val messagesApi: MessagesApi = new DefaultMessagesApi()
  implicit private val messages: Messages = MessagesImpl(defaultLang, messagesApi)

  val form = Form(
    "fieldName" -> Forms.text.verifying("required", _.nonEmpty)
  )

  private val field = form("fieldName")

  "Checkboxes" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val itemContent = TextContent("foo")
        val value = "bar"
        val item = CheckboxItem(field, itemContent, value)

        val describedBy = Some("described by")
        val legend = Some(Legend(TextContent("legend")))
        val fieldset = Some(Fieldset(legend))
        val hint = Some(Hint(TextContent("hint")))
        val formGroupClasses = Some("form group classes")
        val idPrefix = Some("id prefix")
        val classes = Some("classes")
        val attributes = Map("key1" -> "value1")

        val checkboxes = Checkboxes(field, Seq(item), describedBy, fieldset, hint, formGroupClasses, idPrefix, classes, attributes)

        Json.toJson(checkboxes) mustEqual Json.obj(
          "name"        -> "fieldName",
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
          "hint"       -> Json.obj("text" -> "hint"),
          "formGroup"  -> Json.obj("classes" -> "form group classes"),
          "idPrefix"   -> "id prefix",
          "classes"    -> "classes",
          "attributes" -> Json.obj("key1" -> "value1")
        )
      }

      "when no optional values are supplied" in {

        val itemContent = TextContent("foo")
        val value = "bar"
        val item = CheckboxItem(field, itemContent, value)

        val checkboxes = Checkboxes(field, Seq(item))

        Json.toJson(checkboxes) mustEqual Json.obj(
          "name"        -> "fieldName",
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

        an[IllegalArgumentException] must be thrownBy Checkboxes(field, Seq.empty)
      }
    }
  }
}
