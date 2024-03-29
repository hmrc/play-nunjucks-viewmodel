/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.i18n.Messages
import play.api.libs.json.Writes

sealed trait Content

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

  // This is needed from Play 2.8 onwards, as there is an undocumented change of the play.api.libs.json.Writes method
  // from contravariant Writes[-A] to invariant Writes[A] https://github.com/playframework/playframework/issues/10416
  implicit def messageWrites(implicit messages: Messages): Writes[Message] =
    Writes.of[String].contramap[Text.Message](_.resolve)
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
