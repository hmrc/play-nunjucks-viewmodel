package uk.gov.hmrc.viewmodels

import play.api.data.Field
import play.api.i18n.Messages
import play.api.libs.json.{Json, OWrites}

object DateInput {

  final case class ViewModel(items: Seq[Item], error: Option[Message])
  final case class Item(label: Message, name: String, id: String, value: String, classes: String)

  object ViewModel {
    implicit def writes(implicit messages: Messages): OWrites[ViewModel] =
      OWrites {
        viewmodel =>
          Json.obj(
            "items" -> viewmodel.items,
            "error" -> viewmodel.error.map {
              error =>
                Json.obj(
                  "text" -> error
                )
            }
          )
      }
  }

  object Item {
    implicit def writes(implicit messages: Messages): OWrites[Item] = Json.writes[Item]
  }

  def localDate(field: Field): ViewModel = {

    val error = (field.error orElse field("day").error orElse field("month").error orElse field("year").error)
      .map(formError => Message.Computed(formError.message, formError.args: _*))

    def classes(classes: String*): String = {
      val allClasses = if (error.isDefined) "govuk-input--error" :: classes.toList else classes.toList
      allClasses.mkString(" ")
    }

    val items = Seq(
      Item(
        label = Message.Computed("site.day.capitalized"),
        name = field("day").name,
        id = field.id,
        value = field("day").value.getOrElse(""),
        classes = classes("govuk-input--width-2")
      ),
      Item(
        label = Message.Computed("site.month.capitalized"),
        name = field("month").name,
        id = field("month").id,
        value = field("month").value.getOrElse(""),
        classes = classes("govuk-input--width-2")
      ),
      Item(
        label = Message.Computed("site.year.capitalized"),
        name = field("year").name,
        id = field("year").id,
        value = field("year").value.getOrElse(""),
        classes = classes("govuk-input--width-4")
      )
    )

    ViewModel(items, error)
  }
}
