package uk.gov.hmrc.viewmodels

import play.api.libs.json.{Json, OWrites}

final case class Accordion (
                             id: String,
                             items: Seq[AccordionItem],
                             headingLevel: Option[HeadingLevel] = None,
                             classes: Option[String] = None,
                             attributes: Map[String, String] = Map.empty
                           ) {

  require(items.nonEmpty, "items cannot be empty")
}

object Accordion {

  implicit lazy val writes: OWrites[Accordion] = OWrites {
    accordion =>

      Json.obj(
        "id"           -> accordion.id,
        "items"        -> accordion.items,
        "headingLevel" -> accordion.headingLevel,
        "classes"      -> accordion.classes,
        "attributes"   -> accordion.attributes
      ).filterNulls
  }
}
