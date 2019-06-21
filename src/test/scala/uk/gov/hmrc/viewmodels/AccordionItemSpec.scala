package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.libs.json.Json
import play.twirl.api.Html

class AccordionItemSpec extends FreeSpec with MustMatchers with OptionValues {

  "Accordion Item" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val heading = TextContent("foo")
        val content = HtmlContent(Html("<i>bar</i>"))
        val summary = Some(TextContent("baz"))
        val expanded = true

        val accordionItem = AccordionItem(heading, content, summary, expanded)

        Json.toJson(accordionItem) mustEqual Json.obj(
          "heading"  -> Json.obj("text" -> "foo"),
          "content"  -> Json.obj("html" -> "<i>bar</i>"),
          "summary"  -> Json.obj("text" -> "baz"),
          "expanded" -> true
        )
      }

      "when no optional values are supplied" in {

        val heading = TextContent("foo")
        val content = HtmlContent(Html("<i>bar</i>"))

        val accordionItem = AccordionItem(heading, content)

        Json.toJson(accordionItem) mustEqual Json.obj(
          "heading"  -> Json.obj("text" -> "foo"),
          "content"  -> Json.obj("html" -> "<i>bar</i>"),
          "expanded" -> false
        )
      }
    }
  }
}
