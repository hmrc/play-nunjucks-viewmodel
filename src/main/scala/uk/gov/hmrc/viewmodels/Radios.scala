package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}

final case class Radios (
                          name: String,
                          items: Seq[RadioItem],
                          fieldset: Option[Fieldset],
                          hint: Option[Hint],
                          errorMessage: Option[ErrorMessage],
                          formGroupClasses: Option[String],
                          idPrefix: Option[String],
                          classes: Option[String],
                          attributes: Map[String, String]
                        ) {

  require(items.nonEmpty, "items cannot be empty")
}

object Radios {

  def apply(
             field: Field,
             items: Seq[RadioItem],
             fieldset: Option[Fieldset] = None,
             hint: Option[Hint] = None,
             formGroupClasses: Option[String] = None,
             idPrefix: Option[String] = None,
             classes: Option[String] = None,
             attributes: Map[String, String] = Map.empty
           )(implicit messages: Messages): Radios = {

    val errorMessage = field.error.map {
      err =>
        ErrorMessage(TextContent(err.format))
    }

    Radios(field.name, items, fieldset, hint, errorMessage, formGroupClasses, idPrefix, classes, attributes)
  }

  def yesOrNo(
               field: Field,
               fieldset: Option[Fieldset] = None,
               hint: Option[Hint] = None,
               formGroupClasses: Option[String] = None,
               idPrefix: Option[String] = None,
               classes: Option[String] = None,
               attributes: Map[String, String] = Map.empty
             )(implicit messages: Messages): Radios = {


    val yes = RadioItemInput(
      field   = field,
      content = TextContent(messages("site.yes")),
      value   = "true"
    )

    val no = RadioItemInput(
      field   = field,
      content = TextContent(messages("site.no")),
      value   = "false"
    )

    val items = Seq(yes, no)

    val allClasses = Some(("govuk-radios--inline " + classes.getOrElse("")).trim)

    Radios(field, items, fieldset, hint, formGroupClasses, idPrefix, allClasses, attributes)
  }

  implicit def writes(implicit messages: Messages): OWrites[Radios] = OWrites {
    radios =>

      Json.obj(
        "name"             -> radios.name,
        "items"            -> radios.items,
        "fieldset"         -> radios.fieldset,
        "hint"             -> radios.hint,
        "formGroup"        -> Json.obj("classes" -> radios.formGroupClasses).filterNulls,
        "idPrefix"         -> radios.idPrefix,
        "classes"          -> radios.classes,
        "attributes"       -> radios.attributes,
        "errorMessage"     -> radios.errorMessage
      ).filterNulls
  }
}
