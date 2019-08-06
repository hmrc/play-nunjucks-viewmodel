package viewmodels

import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}
import uk.gov.hmrc.viewmodels.{ErrorSummary, Hint, Input, Label, TextContent}

final case class TextboxPageViewModel private (
                                                textbox: Input,
                                                errorSummary: Option[ErrorSummary]
                                              )

object TextboxPageViewModel {

  implicit def writes(implicit messages: Messages): OWrites[TextboxPageViewModel] =
    Json.writes[TextboxPageViewModel]

  def apply(form: Form[_])(implicit messages: Messages): TextboxPageViewModel = {

    val label =
      Label(
        forId         = "value",
        content       = TextContent(messages("name.heading")),
        isPageHeading = true,
        classes       = Some("govuk-label--xl")
      )

    val hint = Hint(TextContent(messages("name.hint")))

    val input = Input(form("value"), label = Some(label), hint = Some(hint))

    val errorSummary = if (form.errors.nonEmpty) {
      Some(ErrorSummary(form.errors))
    } else {
      None
    }

    TextboxPageViewModel(input, errorSummary)
  }
}
