package uk.gov.hmrc.viewmodels

import play.api.libs.json.{Json, OWrites}

final case class AccordionItem (
                                 heading: Content,
                                 content: HtmlContent,
                                 summary: Option[Content] = None,
                                 expanded: Boolean = false
                               )

object AccordionItem {

  implicit lazy val writes: OWrites[AccordionItem] = OWrites {
    item =>

      Json.obj(
        "heading"  -> item.heading,
        "content"  -> item.content,
        "summary"  -> item.summary,
        "expanded" -> item.expanded
      ).filterNulls
  }
}
