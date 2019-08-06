package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.libs.json.{Json, OWrites}

sealed trait RadioItem

object RadioItem {

  implicit lazy val writes: OWrites[RadioItem] = OWrites {
    case input: RadioItemInput => Json.toJsObject(input)(RadioItemInput.writes)
    case divider: Divider      => Json.toJsObject(divider)(Divider.writes)
  }
}

final case class Divider(text: String) extends RadioItem

object Divider {

  lazy val writes: OWrites[Divider] = OWrites {
    divider =>

      Json.obj("divider" -> divider.text)
  }
}

final case class RadioItemInput (
                                  content: Content,
                                  value: String,
                                  id: Option[String],
                                  label: Option[Label],
                                  hint: Option[Hint],
                                  checked: Boolean,
                                  disabled: Boolean,
                                  conditionalHtml: Option[HtmlContent],
                                  attributes: Map[String, String]
                                ) extends RadioItem

object RadioItemInput {

  def apply(
             field: Field,
             content: Content,
             value: String,
             id: Option[String] = None,
             label: Option[Label] = None,
             hint: Option[Hint] = None,
             disabled: Boolean = false,
             conditionalHtml: Option[HtmlContent] = None,
             attributes: Map[String, String] = Map.empty
           ): RadioItem = {

    val checked = field.value.contains(value)

    RadioItemInput(content, value, id, label, hint, checked, disabled, conditionalHtml, attributes)
  }

  lazy val writes: OWrites[RadioItemInput] = OWrites {
    item =>

      Json.toJsObject(item.content) ++ Json.obj(
        "value"       -> item.value,
        "id"          -> item.id,
        "label"       -> item.label,
        "hint"        -> item.hint,
        "checked"     -> item.checked,
        "disabled"    -> item.disabled,
        "conditional" -> Json.obj("html" -> item.conditionalHtml).filterNulls,
        "attributes"  -> item.attributes
      ).filterNulls
  }
}
