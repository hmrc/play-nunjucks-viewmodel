package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}

final case class DateInput (
                             items: Seq[DatePart],
                             id: Option[String],
                             namePrefix: Option[String],
                             hint: Option[Hint],
                             errorMessage: Option[ErrorMessage],
                             formGroupClasses: Option[String],
                             fieldset: Option[Fieldset],
                             classes: Option[String],
                             attributes: Map[String, String]
                           )
object DateInput {

  def apply(
             field: Field,
             id: Option[String] = None,
             namePrefix: Option[String] = None,
             hint: Option[Hint] = None,
             formGroupClasses: Option[String] = None,
             fieldset: Option[Fieldset] = None,
             classes: Option[String] = None,
             attributes: Map[String, String] = Map.empty
           )(implicit messages: Messages): DateInput = {

    val errorMessage = field.error.map {
      err =>
        ErrorMessage(TextContent(err.format))
    }

    val dayField = DatePart(field("day"), classes = Some("govuk-input--width-2"), label = Some(messages("site.day")))
    val monthField = DatePart(field("month"), classes = Some("govuk-input--width-2"), label = Some(messages("site.month")))
    val yearField = DatePart(field("year"), classes = Some("govuk-input--width-4"), label = Some(messages("site.year")))
    val fields = Seq(dayField, monthField, yearField)

    DateInput(fields, id, namePrefix, hint, errorMessage, formGroupClasses, fieldset, classes, attributes)
  }

  implicit lazy val writes: OWrites[DateInput] = OWrites {
    date =>

      Json.obj(
        "items"        -> date.items,
        "id"           -> date.id,
        "namePrefix"   -> date.namePrefix,
        "hint"         -> date.hint,
        "errorMessage" -> date.errorMessage,
        "formGroup"    -> Json.obj("classes" -> date.formGroupClasses).filterNulls,
        "fieldset"     -> date.fieldset,
        "classes"      -> date.classes,
        "attributes"   -> date.attributes
      ).filterNulls
  }
}
