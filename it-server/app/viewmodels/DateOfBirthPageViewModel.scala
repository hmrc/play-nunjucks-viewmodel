package viewmodels

import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}
import uk.gov.hmrc.viewmodels.{DateInput, ErrorSummary, Fieldset, Hint, Legend, TextContent}

final case class DateOfBirthPageViewModel private (
                                                    dateOfBirth: DateInput,
                                                    errorSummary: Option[ErrorSummary]
                                                  )

object DateOfBirthPageViewModel {

  implicit def writes(implicit messages: Messages): OWrites[DateOfBirthPageViewModel] =
    Json.writes[DateOfBirthPageViewModel]

  def apply(form: Form[_])(implicit messages: Messages): DateOfBirthPageViewModel = {

    val legend =
      Legend(
        content       = TextContent(messages("dateOfBirth.heading")),
        isPageHeading = true,
        classes       = Some("govuk-fieldset__legend--xl")
      )

    val fieldset = Fieldset(Some(legend))

    val hint = Hint(TextContent(messages("dateOfBirth.hint")))

    val dateInput = DateInput(
      field = form("value"),
      fieldset = Some(fieldset),
      hint = Some(hint)
    )

    val errorSummary = if (form.errors.nonEmpty) {
      Some(ErrorSummary(form.errors))
    } else {
      None
    }

    DateOfBirthPageViewModel(dateInput, errorSummary)
  }
}
