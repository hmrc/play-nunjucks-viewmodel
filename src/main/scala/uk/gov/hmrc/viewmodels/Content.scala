package uk.gov.hmrc.viewmodels

import play.api.i18n.Messages
import play.api.libs.json.Writes
import play.api.libs.functional.syntax._

sealed trait Content

//  final case class Text(value: Info) extends Content
sealed trait Text extends Content {
  def resolve(implicit messages: Messages): String
}

object Text {

  final case class Message(key: String, args: Any*) extends Text {

    override def resolve(implicit messages: Messages): String = {

      val resolvedArgs = args.map {
        case message: Text => message.resolve
        case other         => other
      }

      Messages(key, resolvedArgs: _*)
    }

    def withArgs(args: Any*): Message =
      Message(key, args: _*)
  }

  final case class Literal(value: String) extends Text {
    override def resolve(implicit messages: Messages): String =
      value
  }

  implicit def writes(implicit messages: Messages): Writes[Text] =
    Writes.of[String].contramap[Text](_.resolve)
}

final case class Html(value: play.twirl.api.Html) extends Content

object Html {

  def apply(content: String): Html =
    new Html(play.twirl.api.Html(content))

  implicit lazy val writes: Writes[Html] =
    Writes.of[String].contramap[Html](_.value.toString)
}

trait WithContent {

  def content: Content

  def text: Option[Text] = content match {
    case c: Text => Some(c)
    case _       => None
  }

  def html: Option[Html] = content match {
    case c: Html => Some(c)
    case _       => None
  }
}
