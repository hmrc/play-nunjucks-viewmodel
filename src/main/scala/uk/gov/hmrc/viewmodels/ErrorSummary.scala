package uk.gov.hmrc.viewmodels

import play.api.data.FormError
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}

final case class ErrorSummary (
                                title: Content,
                                errors: Seq[ErrorSummaryItem],
                                description: Option[Content] = None,
                                classes: Option[String] = None,
                                attributes: Map[String, String] = Map.empty
                              )

object ErrorSummary {

  def apply(errors: Seq[FormError])(implicit messages: Messages): ErrorSummary = {

    val items = errors.map {
      error =>
        ErrorSummaryItem(s"#${error.key}", TextContent(messages(error.message, error.args: _*)))
    }

    ErrorSummary(
      TextContent(messages("error.summary.title")),
      items
    )
  }

  implicit lazy val writes: OWrites[ErrorSummary] = OWrites {
    errorSummary =>

      val titleJson = errorSummary.title match {
        case h: HtmlContent => Json.obj("titleHtml" -> h.value.toString)
        case t: TextContent => Json.obj("titleText" -> t.value)
      }

      val descriptionJson = errorSummary.description.map {
        case h: HtmlContent => Json.obj("descriptionHtml" -> h.value.toString)
        case t: TextContent => Json.obj("descriptionText" -> t.value)
      }.getOrElse(Json.obj())

      titleJson ++ descriptionJson ++ Json.obj(
        "errorList"  -> errorSummary.errors,
        "classes"    -> errorSummary.classes,
        "attributes" -> errorSummary.attributes
      ).filterNulls
  }
}

final case class ErrorSummaryItem (
                                    href: String,
                                    content: Content,
                                    attributes: Map[String, String] = Map.empty
                                  )

object ErrorSummaryItem {

  implicit lazy val writes: OWrites[ErrorSummaryItem] = OWrites {
    item =>

      Json.toJsObject(item.content) ++ Json.obj(
        "href"       -> item.href,
        "attributes" -> item.attributes
      ).filterNulls
  }
}
