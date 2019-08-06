package models

import play.api.data.Field
import play.api.i18n.Messages
import uk.gov.hmrc.viewmodels.{CheckboxItem, TextContent}

sealed trait Location

object Location extends Enumerable.Implicits {

  case object England extends WithName("england") with Location
  case object Scotland extends WithName("scotland") with Location
  case object Wales extends WithName("wales") with Location
  case object NorthernIreland extends WithName("northernIreland") with Location

  val values: Seq[Location] = Seq(
    England, Scotland, Wales, NorthernIreland
  )

  def checkboxItems(field: Field)(implicit messages: Messages): Seq[CheckboxItem] = values.map {
    value =>

      CheckboxItem(field, TextContent(messages(s"location.${value.toString}")), value.toString)
  }
}
