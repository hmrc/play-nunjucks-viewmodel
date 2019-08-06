package viewmodels

import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}
import uk.gov.hmrc.viewmodels.{ErrorSummary, Fieldset, Legend, Radios, TextContent}

final case class LivesInUKPageViewModel private (
                                                  radios: Radios,
                                                  errorSummary: Option[ErrorSummary]
                                                )

object LivesInUKPageViewModel {

  implicit def writes(implicit messages: Messages): OWrites[LivesInUKPageViewModel] =
    Json.writes[LivesInUKPageViewModel]

  def apply(form: Form[_])(implicit messages: Messages): LivesInUKPageViewModel = {

    val legend =
      Legend(
        content       = TextContent(messages("livesInUK.heading")),
        isPageHeading = true,
        classes       = Some("govuk-fieldset__legend--xl")
      )

    val fieldset = Fieldset(Some(legend))

    val radios = Radios.yesOrNo(
      field = form("value"),
      fieldset = Some(fieldset)
    )

    val errorSummary = if (form.errors.nonEmpty) {
      Some(ErrorSummary(form.errors))
    } else {
      None
    }

    LivesInUKPageViewModel(radios, errorSummary)
  }
}
