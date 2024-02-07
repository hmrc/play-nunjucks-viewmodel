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
import play.api.libs.json._
import play.api.libs.functional.syntax._

object SummaryList {

  final case class Key(content: Content, classes: Seq[String] = Seq.empty) extends WithContent

  object Key {

    implicit def writes(implicit messages: Messages): OWrites[Key] = (
      (__ \ "text").writeNullable[Text] and
        (__ \ "html").writeNullable[Html] and
        (__ \ "classes").writeNullable[String]
    )(key => (key.text, key.html, classes(key.classes)))
  }

  final case class Value(content: Content, classes: Seq[String] = Seq.empty) extends WithContent

  object Value {

    implicit def writes(implicit messages: Messages): OWrites[Value] = (
      (__ \ "text").writeNullable[Text] and
        (__ \ "html").writeNullable[Html] and
        (__ \ "classes").writeNullable[String]
    )(value => (value.text, value.html, classes(value.classes)))
  }

  final case class Action(
    content: Content,
    href: String,
    visuallyHiddenText: Option[Text] = None,
    classes: Seq[String] = Seq.empty,
    attributes: Map[String, String] = Map.empty
  ) extends WithContent

  object Action {

    implicit def writes(implicit messages: Messages): OWrites[Action] = (
      (__ \ "href").write[String] and
        (__ \ "text").writeNullable[Text] and
        (__ \ "html").writeNullable[Html] and
        (__ \ "visuallyHiddenText").writeNullable[Text] and
        (__ \ "classes").writeNullable[String] and
        (__ \ "attributes").writeNullable[Map[String, String]]
    ) { action =>
      val attributes = Some(action.attributes).filter(_.nonEmpty)
      (action.href, action.text, action.html, action.visuallyHiddenText, classes(action.classes), attributes)
    }
  }

  final case class Row(key: Key, value: Value, actions: Seq[Action] = Seq.empty)

  object Row {

    implicit def writes(implicit messages: Messages): OWrites[Row] = (
      (__ \ "key").write[Key] and
        (__ \ "value").write[Value] and
        (__ \ "actions" \ "items").writeNullable[Seq[Action]]
    ) { row =>
      val actions = Some(row.actions).filter(_.nonEmpty)
      (row.key, row.value, actions)
    }
  }

  private def classes(classes: Seq[String]): Option[String] =
    if (classes.isEmpty) None else Some(classes.mkString(" "))
}
