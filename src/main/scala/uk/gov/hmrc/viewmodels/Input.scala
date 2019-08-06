package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}

final case class Input private (
                                 id: String,
                                 name: String,
                                 inputType: InputType,
                                 value: Option[String],
                                 describedBy: Option[String],
                                 label: Option[Label],
                                 hint: Option[Hint],
                                 errorMessage: Option[ErrorMessage],
                                 formGroupClasses: Option[String],
                                 classes: Option[String],
                                 autocomplete: Option[String],
                                 pattern: Option[String],
                                 attributes: Map[String, String]
                               )

object Input {

  def apply(
             field: Field,
             inputType: InputType = InputType.Text,
             describedBy: Option[String] = None,
             label: Option[Label] = None,
             hint: Option[Hint] = None,
             formGroupClasses: Option[String] = None,
             classes: Option[String] = None,
             autocomplete: Option[String] = None,
             pattern: Option[String] = None,
             attributes: Map[String, String] = Map.empty
           )(implicit messages: Messages): Input = {

    val errorMessage = field.error.map {
      err =>
        ErrorMessage(TextContent(err.format))
    }

    Input(field.id, field.name, inputType, field.value, describedBy, label, hint, errorMessage, formGroupClasses, classes, autocomplete, pattern, attributes)
  }

  implicit def writes(implicit messages: Messages): OWrites[Input] = OWrites {
    input =>

      Json.obj(
        "id"              -> input.id,
        "name"            -> input.name,
        "inputType"       -> input.inputType,
        "value"           -> input.value,
        "describedBy"     -> input.describedBy,
        "label"           -> input.label,
        "hint"            -> input.hint,
        "errorMessage"    -> input.errorMessage,
        "formGroup"       -> Json.obj("classes" -> input.formGroupClasses),
        "classes"         -> input.classes,
        "autocomplete"    -> input.autocomplete,
        "pattern"         -> input.pattern,
        "attributes"      -> input.attributes
      ).filterNulls
  }
}
