package uk.gov.hmrc.viewmodels

import play.api.libs.json.{Json, OWrites}

final case class Label (
                         forId: String,
                         content: Content,
                         isPageHeading: Boolean = false,
                         classes: Option[String] = None,
                         attributes: Map[String, String] = Map.empty
                       )

object Label {

  implicit lazy val writes: OWrites[Label] = OWrites {
    label =>

      Json.toJsObject(label.content) ++ Json.obj(
        "for"           -> label.forId,
        "isPageHeading" -> label.isPageHeading,
        "classes"       -> label.classes,
        "attributes"    -> label.attributes
      ).filterNulls
  }
}
