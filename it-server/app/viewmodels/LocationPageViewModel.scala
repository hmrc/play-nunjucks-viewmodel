package viewmodels

import models.Location
import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}
import uk.gov.hmrc.viewmodels._

final case class LocationPageViewModel private(
                                                   checkboxes: Checkboxes,
                                                   errorSummary: Option[ErrorSummary]
                                                 )

object LocationPageViewModel {

  implicit def writes(implicit messages: Messages): OWrites[LocationPageViewModel] =
    Json.writes[LocationPageViewModel]

  def apply(form: Form[_])(implicit messages: Messages): LocationPageViewModel = {

    val legend =
      Legend(
        content       = TextContent(messages("location.heading")),
        isPageHeading = true,
        classes       = Some("govuk-fieldset__legend--xl")
      )

    val fieldset = Fieldset(Some(legend))

    val checkboxes = Checkboxes(
      field    = form("value"),
      items    = Location.checkboxItems(form("value")),
      fieldset = Some(fieldset)
    )

    val errorSummary = if (form.errors.nonEmpty) {
      Some(ErrorSummary(form.errors))
    } else {
      None
    }

    LocationPageViewModel(checkboxes, errorSummary)
  }
}
