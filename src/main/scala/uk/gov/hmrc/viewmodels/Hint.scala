package uk.gov.hmrc.viewmodels

import play.api.libs.json.{Json, OWrites}

final case class Hint (
                        content: Content,
                        id: Option[String] = None,
                        classes: Option[String] = None,
                        attributes: Map[String, String] = Map.empty
                      )

object Hint {

  implicit lazy val writes: OWrites[Hint] = OWrites {
    hint =>

      Json.toJsObject(hint.content) ++ Json.obj(
        "id"         -> hint.id,
        "classes"    -> hint.classes,
        "attributes" -> hint.attributes
      ).filterNulls
  }
}
