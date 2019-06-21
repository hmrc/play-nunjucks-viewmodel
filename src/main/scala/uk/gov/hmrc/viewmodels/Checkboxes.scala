package uk.gov.hmrc.viewmodels

import play.api.libs.json.{Json, OWrites}

final case class Checkboxes (
                              name: String,
                              items: Seq[CheckboxItem],
                              describedBy: Option[String] = None,
                              fieldset: Option[Fieldset] = None,
                              hint: Option[Hint] = None,
                              formGroupClasses: Option[String] = None,
                              idPrefix: Option[String] = None,
                              classes: Option[String] = None,
                              attributes: Map[String, String] = Map.empty
                            ) {

  require(items.nonEmpty, "items cannot be empty")
}

object Checkboxes {

  implicit lazy val writes: OWrites[Checkboxes] = OWrites {
    checkboxes =>

      Json.obj(
        "name"             -> checkboxes.name,
        "items"            -> checkboxes.items,
        "describedBy"      -> checkboxes.describedBy,
        "fieldset"         -> checkboxes.fieldset,
        "hint"             -> checkboxes.hint,
        "formGroupClasses" -> checkboxes.formGroupClasses,
        "idPrefix"         -> checkboxes.idPrefix,
        "classes"          -> checkboxes.classes,
        "attributes"       -> checkboxes.attributes
      ).filterNulls
  }
}
