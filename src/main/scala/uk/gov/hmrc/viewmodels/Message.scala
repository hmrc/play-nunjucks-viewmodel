package uk.gov.hmrc.viewmodels

import play.api.i18n.Messages
import play.api.libs.json.{JsString, Writes}

trait Message {
  def resolve(implicit messages: Messages): String
}

object Message {

  final case class Literal(value: String) extends Message {
    override def resolve(implicit messages: Messages): String =
      value
  }

  final case class Computed(key: String, args: Any*) extends Message {

    override def resolve(implicit messages: Messages): String = {

      val resolvedArgs = args.map {
        case message: Message => message.resolve
        case other            => other
      }

      Messages(key, resolvedArgs: _*)
    }
  }

  implicit def writes(implicit messages: Messages): Writes[Message] =
    Writes {
      message =>
        JsString(message.resolve)
    }
}
