package uk.gov.hmrc.viewmodels

import play.api.libs.json.{JsString, Writes}

sealed trait InputType

object InputType {

  case object Button extends WithName("button") with InputType
  case object Checkbox extends WithName("checkbox") with InputType
  case object Color extends WithName("color") with InputType
  case object Date extends WithName("date") with InputType
  case object DatetimeLocal extends WithName("datetime-local") with InputType
  case object Email extends WithName("email") with InputType
  case object File extends WithName("file") with InputType
  case object Hidden extends WithName("hidden") with InputType
  case object Image extends WithName("image") with InputType
  case object Month extends WithName("month") with InputType
  case object Number extends WithName("number") with InputType
  case object Password extends WithName("password") with InputType
  case object Radio extends WithName("radio") with InputType
  case object Range extends WithName("range") with InputType
  case object Reset extends WithName("reset") with InputType
  case object Search extends WithName("search") with InputType
  case object Submit extends WithName("submit") with InputType
  case object Tel extends WithName("tel") with InputType
  case object Text extends WithName("text") with InputType
  case object Time extends WithName("time") with InputType
  case object Url extends WithName("url") with InputType
  case object Week extends WithName("week") with InputType

  implicit lazy val writes: Writes[InputType] = Writes {
    inputType =>

      JsString(inputType.toString)
  }
}
