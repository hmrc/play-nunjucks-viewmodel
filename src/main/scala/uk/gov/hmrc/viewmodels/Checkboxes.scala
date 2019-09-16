package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.libs.json.{Json, OWrites}

object Checkboxes {

  final case class BooleanProduct(label: String, name: String)
  final case class Checkbox(label: String, value: String)

  final case class Item(name: String, id: String, text: String, value: String, checked: Boolean)

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
          text    = item.label,
          value   = item.value,
          checked = field.values.contains(item.value)
        )
    }

    val tail = items.tail.zipWithIndex.map {
      case (item, i) =>
        Item(
          name    = s"${field.name}[${i + 1}]",
          id      = s"${field.id}_${i + 1}",
          text    = item.label,
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
          text    = item.label,
          value   = "true",
          checked = field(item.name).value.contains("true")
        )
    }

    val tail = items.tail.map {
      item =>
        Item(
          name    = field(item.name).name,
          id      = field(item.name).id,
          text    = item.label,
          value   = "true",
          checked = field(item.name).value.contains("true")
        )
    }

    head.toSeq ++ tail
  }
}
