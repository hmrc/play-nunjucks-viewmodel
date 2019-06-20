package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.libs.json.Json
import play.twirl.api.Html

class ContentSpec extends FreeSpec with MustMatchers {

  "Content" - {

    "must serialise Text Content" in {

      val text = TextContent("foo")

      Json.toJson[Content](text) mustEqual Json.obj("text" -> "foo")
    }

    "must serialise Html Content" in {

      val html = HtmlContent(Html("<p>foo</p>"))

      Json.toJson[Content](html) mustEqual Json.obj("html" -> "<p>foo</p>")
    }
  }
}
