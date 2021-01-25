/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.i18n.Messages
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Checkboxes {

  final case class BooleanProduct(label: Content, name: String)
  final case class Checkbox(label: Content, value: String)

  final case class Item(name: String, id: String, content: Content, value: String, checked: Boolean) extends WithContent

  object Item {

    implicit def writes(implicit messages: Messages): OWrites[Item] = (
      (__ \ "name").write[String] and
      (__ \ "id").write[String] and
      (__ \ "text").writeNullable[Text] and
      (__ \ "html").writeNullable[Html] and
      (__ \ "value").write[String] and
      (__ \ "checked").write[Boolean]
    ){ item =>
      (item.name, item.id, item.text, item.html, item.value, item.checked)
    }
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
