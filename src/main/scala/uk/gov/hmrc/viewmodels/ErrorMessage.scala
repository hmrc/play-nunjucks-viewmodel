package uk.gov.hmrc.viewmodels

import play.api.libs.json.{Json, OWrites}

final case class ErrorMessage (
                                content: Content,
                                id: Option[String] = None,
                                visuallyHiddenText: Option[String] = None,
                                classes: Option[String] = None,
                                attributes: Map[String, String] = Map.empty
                              )

object ErrorMessage {

  implicit lazy val writes: OWrites[ErrorMessage] = OWrites{
    errorMessage =>

      Json.toJsObject(errorMessage.content) ++ Json.obj(
        "id"                 -> errorMessage.id,
        "visuallyHiddenText" -> errorMessage.visuallyHiddenText,
        "classes"            -> errorMessage.classes,
        "attributes"         -> errorMessage.attributes
      ).filterNulls
  }
}
