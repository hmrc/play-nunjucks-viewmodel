package uk.gov.hmrc.viewmodels

import play.api.libs.json._
import play.twirl.api.Html

sealed trait Content

object Content {

  implicit lazy val writes: OWrites[Content] = OWrites {
    case t: TextContent => Json.toJsObject(t)(TextContent.writes)
    case h: HtmlContent => Json.toJsObject(h)(HtmlContent.writes)
  }
}

final case class HtmlContent(value: Html) extends Content

object HtmlContent {

  lazy val writes: OWrites[HtmlContent] = OWrites {
    content =>

      Json.obj("html" -> content.value.toString)
  }
}

final case class TextContent(value: String) extends Content

object TextContent {

  lazy val writes: OWrites[TextContent] = OWrites {
    content =>

      Json.obj("text" -> content.value)
  }
}
