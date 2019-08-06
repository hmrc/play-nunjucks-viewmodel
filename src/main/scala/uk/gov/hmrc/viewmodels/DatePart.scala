package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.libs.json.{Json, OWrites}

final case class DatePart (
                            name: String,
                            id: Option[String],
                            value: Option[String],
                            label: Option[String],
                            autocomplete: Option[String],
                            pattern: Option[String],
                            classes: Option[String],
                            attributes: Map[String, String]
                          )

object DatePart {

  def apply(
             field: Field,
             label: Option[String] = None,
             autocomplete: Option[String] = None,
             pattern: Option[String] = None,
             classes: Option[String] = None,
             attributes: Map[String, String] = Map.empty
           ): DatePart = {

    DatePart(field.name, Some(field.id), field.value, label, autocomplete, pattern, classes, attributes)
  }

  implicit lazy val writes: OWrites[DatePart] = OWrites {
    datePart =>

      Json.obj(
        "name"         -> datePart.name,
        "id"           -> datePart.id,
        "label"        -> datePart.label,
        "value"        -> datePart.value,
        "autocomplete" -> datePart.autocomplete,
        "pattern"      -> datePart.pattern,
        "classes"      -> datePart.classes,
        "attributes"   -> datePart.attributes
      ).filterNulls
  }
}
