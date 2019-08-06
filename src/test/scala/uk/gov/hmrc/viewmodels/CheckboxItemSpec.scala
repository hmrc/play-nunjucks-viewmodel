package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.data.{Form, Forms}
import play.api.i18n.{DefaultMessagesApi, Messages, MessagesApi, MessagesImpl}
import play.api.i18n.Lang.defaultLang
import play.api.libs.json.Json
import play.twirl.api.Html

class CheckboxItemSpec extends FreeSpec with MustMatchers {

  implicit private val messagesApi: MessagesApi = new DefaultMessagesApi()
  implicit private val messages: Messages = MessagesImpl(defaultLang, messagesApi)

  val form = Form(
    "value" -> Forms.text.verifying("required", _.nonEmpty)
  )

  private val field = form("value")

  "Checkbox Item" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val content = TextContent("foo")
        val value = "value"
        val id = Some("id")
        val name = Some("name")
        val hint = Some(Hint(TextContent("hint")))
        val disabled = true
        val conditionalHtml = Some(HtmlContent(Html("<i>foo</i>")))
        val labelClasses = Some("bar")
        val attributes = Map("key1" -> "value1")
        val labelAttributes = Map("key2" -> "value2")

        val boundForm = form.bind(Map("value" -> "value"))

        val item = CheckboxItem(boundForm("value"), content, value, id, name, hint, disabled, conditionalHtml, attributes, labelClasses, labelAttributes)

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
        val item = CheckboxItem(field, content, value)

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
