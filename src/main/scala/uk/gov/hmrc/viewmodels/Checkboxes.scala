package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}

final case class Checkboxes private (
                                      name: String,
                                      items: Seq[CheckboxItem],
                                      describedBy: Option[String],
                                      fieldset: Option[Fieldset],
                                      hint: Option[Hint],
                                      formGroupClasses: Option[String],
                                      idPrefix: Option[String],
                                      classes: Option[String],
                                      attributes: Map[String, String],
                                      errorMessage: Option[ErrorMessage]
                                    ) {

  require(items.nonEmpty, "items cannot be empty")
}

object Checkboxes {

  def apply(
             field: Field,
             items: Seq[CheckboxItem],
             describedBy: Option[String] = None,
             fieldset: Option[Fieldset] = None,
             hint: Option[Hint] = None,
             formGroupClasses: Option[String] = None,
             idPrefix: Option[String] = None,
             classes: Option[String] = None,
             attributes: Map[String, String] = Map.empty
           )(implicit messages: Messages): Checkboxes = {

    val errorMessage = field.error.map {
      err =>
        ErrorMessage(TextContent(err.format))
    }

    Checkboxes(field.name, items, describedBy, fieldset, hint, formGroupClasses, idPrefix, classes, attributes, errorMessage)
  }

  implicit def writes(implicit messages: Messages): OWrites[Checkboxes] = OWrites {
    checkboxes =>

      Json.obj(
        "name"             -> checkboxes.name,
        "items"            -> checkboxes.items,
        "describedBy"      -> checkboxes.describedBy,
        "fieldset"         -> checkboxes.fieldset,
        "hint"             -> checkboxes.hint,
        "formGroup"        -> Json.obj("classes" -> checkboxes.formGroupClasses).filterNulls,
        "idPrefix"         -> checkboxes.idPrefix,
        "classes"          -> checkboxes.classes,
        "attributes"       -> checkboxes.attributes,
        "errorMessage"     -> checkboxes.errorMessage
      ).filterNulls
  }
}
