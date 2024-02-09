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

import play.api.data.{Field, Form, Mapping}
import play.api.i18n.Messages
import play.api.libs.json.{JsPath, Json, OWrites, __}

trait NunjucksSupport {

  implicit class MessageInterpolators(sc: StringContext) {

    def msg(args: Any*): Text.Message =
      Text.Message(sc.s(args: _*))

    def lit(args: Any*): Text.Literal =
      Text.Literal(sc.s(args: _*))
  }

  implicit class RichField(field: Field) {

    def values: Seq[String] =
      field.value.toSeq ++ field.indexes.flatMap { i =>
        field(s"[$i]").value
      }
  }

  implicit def formOWrites[A](implicit messages: Messages): OWrites[Form[A]] =
    OWrites { form =>
      def unfoldMappings(mapping: Mapping[_]): List[Mapping[_]] =
        mapping :: mapping.mappings
          .filterNot(_ == mapping)
          .flatMap(unfoldMappings)
          .map { m =>
            val prefix = Some(mapping.key)
              .filter { prefix =>
                prefix.nonEmpty && !m.key.startsWith(prefix)
              }
            prefix.map(m.withPrefix).getOrElse(m)
          }
          .toList

      unfoldMappings(form.mapping)
        .map { m =>
          form.apply(m.key)
        }
        .foldLeft(Json.obj()) { (obj, field) =>
          val path = field.name
            .split("\\.")
            .foldLeft[JsPath](JsPath)(_ \ _)

          val error = field.errors.headOption
            .map { error =>
              Json.obj(
                "error" ->
                  Json.obj("text" -> Text.Message(error.message, error.args: _*))
              )
            }
            .getOrElse(Json.obj())

          obj.validate {
            __.json.update {
              path.json.put {
                Json.obj(
                  "value"  -> field.value,
                  "values" -> field.values
                ) ++ error
              }
            }
            // Can this fail?
          }.get
        } ++ Json.obj(
        "errors" -> form.errors.map { error =>
          Json.obj(
            "text" -> Text.Message(error.message, error.args: _*),
            "href" -> ("#" + form(error.key).id)
          )
        }
      )
    }
}
