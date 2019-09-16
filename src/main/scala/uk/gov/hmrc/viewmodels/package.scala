package uk.gov.hmrc

import play.api.data.{Field, Form, Mapping}
import play.api.i18n.Messages
import play.api.libs.json.{JsArray, JsNull, JsObject, JsValue, Json, OWrites}

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
          mapping :: mapping.mappings.filterNot(_ == mapping).flatMap(unfoldMappings).toList

        unfoldMappings(form.mapping).map {
          m =>
            form.apply(m.key)
        }.foldLeft(Json.obj()) {
          (obj, field) =>

            val error = field.error.map {
              error =>
                Json.obj(
                  "error" ->
                    Json.obj("text" -> messages(error.message, error.args: _*))
                )
            }.getOrElse(Json.obj())

            obj ++ Json.obj(
              field.name ->
                (Json.obj(
                  "value" -> field.value,
                  "values" -> field.values
                ) ++ error)
            )
        } ++ Json.obj(
          "errors" -> form.errors.map {
            error =>
              Json.obj(
                "text" -> messages(error.message, error.args: _*),
                "href" -> ("#" + form(error.key).id)
              )
          }
        )
    }

  implicit class RichJsObject(obj: JsObject) {

    def map(f: (String, JsValue) => (String, JsValue)): JsObject =
      JsObject(obj.fields.map(f.tupled))

    def filter(f: (String, JsValue) => Boolean): JsObject =
      JsObject(obj.fields.filter(f.tupled))

    def filterNot(f: (String, JsValue) => Boolean): JsObject =
      JsObject(obj.fields.filterNot(f.tupled))

    def filterNulls: JsObject = {
      map {
        case (k, v: JsObject) =>
          k -> v.filterNulls
        case (k, v: JsArray) =>
          k -> v.filterNulls
        case (k, v) =>
          k -> v
      }.filterNot((_, value) => value == JsNull || value == Json.obj())
    }
  }

  implicit class RichJsArray(arr: JsArray) {

    def map(f: JsValue => JsValue): JsArray =
      JsArray(arr.value.map(f))

    def filter(f: JsValue => Boolean): JsArray =
      JsArray(arr.value.filter(f))

    def filterNot(f: JsValue => Boolean): JsArray =
      JsArray(arr.value.filterNot(f))

    def filterNulls: JsArray = {
      map {
        case v: JsObject => v.filterNulls
        case v: JsArray  => v.filterNulls
        case v => v
      }.filterNot(v => v == JsNull || v == Json.obj())
    }
  }
}
