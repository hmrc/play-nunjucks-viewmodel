package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers, OptionValues}
import play.api.libs.json.Json
import play.twirl.api.Html

class AccordionSpec extends FreeSpec with MustMatchers with OptionValues {

  "Accordion" - {

    "must serialise" - {

      "when all optional values are supplied" in {

        val heading = TextContent("foo")
        val content = HtmlContent(Html("<i>bar</i>"))
        val summary = Some(TextContent("baz"))
        val expanded = true

        val accordionItem = AccordionItem(heading, content, summary, expanded)

        val headingLevel = Some(HeadingLevel.H1)
        val classes = Some("foo")
        val attributes = Map("key1" -> "value1")

        val accordion = Accordion("id", Seq(accordionItem), headingLevel, classes, attributes)

        Json.toJson(accordion) mustEqual Json.obj(
          "id" -> "id",
          "items" -> Json.arr(
            Json.obj(
              "heading"  -> Json.obj("text" -> "foo"),
              "content"  -> Json.obj("html" -> "<i>bar</i>"),
              "summary"  -> Json.obj("text" -> "baz"),
              "expanded" -> true
            )
          ),
          "headingLevel" -> 1,
          "classes" -> "foo",
          "attributes" -> Json.obj("key1" -> "value1")
        )
      }

      "when no optional values are supplied" in {

        val heading = TextContent("foo")
        val content = HtmlContent(Html("<i>bar</i>"))
        val summary = Some(TextContent("baz"))
        val expanded = true

        val accordionItem = AccordionItem(heading, content, summary, expanded)

        val accordion = Accordion("id", Seq(accordionItem))

        Json.toJson(accordion) mustEqual Json.obj(
          "id" -> "id",
          "items" -> Json.arr(
            Json.obj(
              "heading"  -> Json.obj("text" -> "foo"),
              "content"  -> Json.obj("html" -> "<i>bar</i>"),
              "summary"  -> Json.obj("text" -> "baz"),
              "expanded" -> true
            )
          )
        )
      }
    }

    "must throw an exception" - {

      "when constructed with an empty sequence of items" in {

        an[IllegalArgumentException] must be thrownBy Accordion("id", Seq.empty)
      }
    }
  }
}
