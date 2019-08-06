package uk.gov.hmrc.viewmodels

import play.api.libs.json._

final case class Legend (
                          content: Content,
                          classes: Option[String] = None,
                          isPageHeading: Boolean = false
                        )

object Legend {

  implicit lazy val writes: Writes[Legend] = Writes {
    legend =>

      Json.toJsObject(legend.content) ++ Json.obj(
        "isPageHeading" -> legend.isPageHeading,
        "classes" -> legend.classes
      ).filterNulls
  }
}
