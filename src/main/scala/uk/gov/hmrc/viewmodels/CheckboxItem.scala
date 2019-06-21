package uk.gov.hmrc.viewmodels

import play.api.libs.json.{Json, OWrites}

final case class CheckboxItem (
                                content: Content,
                                value: String,
                                id: Option[String] = None,
                                name: Option[String] = None,
                                hint: Option[Hint] = None,
                                checked: Boolean = false,
                                disabled: Boolean = false,
                                conditionalHtml: Option[HtmlContent] = None,
                                attributes: Map[String, String] = Map.empty,
                                labelClasses: Option[String] = None,
                                labelAttributes: Map[String, String] = Map.empty
                              )

object CheckboxItem {

  implicit lazy val writes: OWrites[CheckboxItem] = OWrites {
    item =>

      val labelJson = Json.obj(
        "classes"    -> item.labelClasses,
        "attributes" -> item.labelAttributes
      ).filterNulls

      Json.toJsObject(item.content) ++ Json.obj(
        "value"       -> item.value,
        "id"          -> item.id,
        "name"        -> item.name,
        "hint"        -> item.hint,
        "checked"     -> item.checked,
        "disabled"    -> item.disabled,
        "conditional" -> item.conditionalHtml,
        "label"       -> labelJson,
        "attributes"  -> item.attributes
      ).filterNulls
  }
}
