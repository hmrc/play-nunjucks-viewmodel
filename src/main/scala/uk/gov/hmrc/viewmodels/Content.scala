package uk.gov.hmrc.viewmodels

import play.api.libs.json._
import play.twirl.api.Html

import play.api.libs.functional.syntax._

sealed trait Content

object Content {

  implicit lazy val writes: OWrites[Content] = OWrites {
    case t: TextContent => Json.toJsObject(t)(TextContent.writes)
    case h: HtmlContent => Json.toJsObject(h)(HtmlContent.writes)
  }
}

final case class HtmlContent(value: Html) extends Content

object HtmlContent {

  implicit lazy val writes: OWrites[HtmlContent] =
    (__ \ "html").write[String].contramap { h: HtmlContent => h.value.toString }
}

final case class TextContent(value: String) extends Content

object TextContent {

  implicit lazy val writes: OWrites[TextContent] =
    (__ \ "text").write[String].contramap { t: TextContent => t.value }
}
