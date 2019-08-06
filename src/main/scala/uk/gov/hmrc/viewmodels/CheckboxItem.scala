package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.libs.json.{Json, OWrites}

final case class CheckboxItem private (
                                        content: Content,
                                        value: String,
                                        id: Option[String],
                                        name: Option[String],
                                        hint: Option[Hint],
                                        checked: Boolean,
                                        disabled: Boolean,
                                        conditionalHtml: Option[HtmlContent],
                                        attributes: Map[String, String],
                                        labelClasses: Option[String],
                                        labelAttributes: Map[String, String]
                                      )

object CheckboxItem {

  def apply(
             field: Field,
             content: Content,
             value: String,
             id: Option[String] = None,
             name: Option[String] = None,
             hint: Option[Hint] = None,
             disabled: Boolean = false,
             conditionalHtml: Option[HtmlContent] = None,
             attributes: Map[String, String] = Map.empty,
             labelClasses: Option[String] = None,
             labelAttributes: Map[String, String] = Map.empty
           ): CheckboxItem = {

    val checked = field.value.contains(value)

    CheckboxItem(content, value, id, name, hint, checked, disabled, conditionalHtml, attributes, labelClasses, labelAttributes)
  }

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
