package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.libs.json.{Json, OWrites}

object Checkboxes {

  final case class BooleanProduct(label: Content, name: String)
  final case class Checkbox(label: Content, value: String)

  final case class Item(name: String, id: String, content: Content, value: String, checked: Boolean)

  object Item {
    implicit lazy val writes: OWrites[Item] =
      Json.writes[Item]
  }

  def set(field: Field, items: Seq[Checkbox]): Seq[Item] = {

    val head = items.headOption.map {
      item =>
        Item(
          name    = s"${field.name}[0]",
          id      = field.id,
          content = item.label,
          value   = item.value,
          checked = field.values.contains(item.value)
        )
    }

    val tail = items.zipWithIndex.tail.map {
      case (item, i) =>
        Item(
          name    = s"${field.name}[$i]",
          id      = s"${field.id}_$i",
          content    = item.label,
          value   = item.value,
          checked = field.values.contains(item.value)
        )
    }

    head.toSeq ++ tail
  }

  def booleanProduct(field: Field, items: Seq[BooleanProduct]): Seq[Item] = {

    val head = items.headOption.map {
      item =>
        Item(
          name = field(item.name).name,
          id      = field.id,
          content    = item.label,
          value   = "true",
          checked = field(item.name).value.contains("true")
        )
    }

    val tail = items.tail.map {
      item =>
        Item(
          name    = field(item.name).name,
          id      = field(item.name).id,
          content    = item.label,
          value   = "true",
          checked = field(item.name).value.contains("true")
        )
    }

    head.toSeq ++ tail
  }
}
