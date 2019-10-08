package uk.gov.hmrc

import play.api.data.{Field, Form, Mapping}
import play.api.i18n.Messages
import play.api.libs.json._

package object viewmodels {

  implicit class RichField(field: Field) {

    def values: Seq[String] = {
      field.value.toSeq ++ field.indexes.flatMap {
        i =>
          field(s"[$i]").value
      }
    }
  }

  implicit def formOWrites[A](implicit messages: Messages): OWrites[Form[A]] =
    OWrites {
      form =>

        def unfoldMappings(mapping: Mapping[_]): List[Mapping[_]] =
          mapping :: mapping.mappings
            .filterNot(_ == mapping)
            .flatMap(unfoldMappings)
            .map {
              m =>
                val prefix = Some(mapping.key)
                  .filter {
                    prefix =>
                      prefix.nonEmpty && !m.key.startsWith(prefix)
                  }
                prefix.map(m.withPrefix).getOrElse(m)
            }
            .toList

        unfoldMappings(form.mapping).map {
          m =>
            form.apply(m.key)
        }.foldLeft(Json.obj()) {
          (obj, field) =>

            val path = field.name.split("\\.")
              .foldLeft[JsPath](JsPath)(_ \ _)

            val error = field.errors.headOption.map {
              error =>
                Json.obj(
                  "error" ->
                    Json.obj("text" -> Message.Computed(error.message, error.args: _*))
                )
            }.getOrElse(Json.obj())

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
          "errors" -> form.errors.map {
            error =>
              Json.obj(
                "text" -> Message.Computed(error.message, error.args: _*),
                "href" -> ("#" + form(error.key).id)
              )
          }
        )
    }
}
