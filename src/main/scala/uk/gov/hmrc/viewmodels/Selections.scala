package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.i18n.Messages
import play.api.libs.functional.syntax._
import play.api.libs.json.{OWrites, __}



object Selections {

  final case class BooleanProduct( label: Content, name: String )

  final case class Select( label: Content, value: String )

  final case class Item( content: Content, value: String ) extends WithContent


  object Item {
    implicit def writes( implicit messages: Messages ): OWrites[Item] = (
      // Json.writes[Select]
      (__ \ "text").writeNullable[Text] and
        (__ \ "html").writeNullable[Html] and
        (__ \ "value").write[String]
      ) { item =>
      (item.text, item.html, item.value)
    }
  }


  def set( field: Field, items: Seq[Select] ): Seq[Item] = {

    val head = items.headOption.map {
      item =>
        Item(
          content = item.label,
          value = item.value
        )
    }

    val tail = items.zipWithIndex.tail.map {
      case (item, i) =>
        Item(
          content = item.label,
          value = item.value
        )
    }

    head.toSeq ++ tail
  }

}
