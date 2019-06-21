package uk.gov.hmrc.viewmodels

import play.api.libs.json.{JsNumber, Writes}

sealed trait HeadingLevel

object HeadingLevel {

  case object H1 extends HeadingLevel
  case object H2 extends HeadingLevel
  case object H3 extends HeadingLevel
  case object H4 extends HeadingLevel
  case object H5 extends HeadingLevel
  case object H6 extends HeadingLevel

  implicit lazy val writes: Writes[HeadingLevel] = Writes {
    case H1 => JsNumber(1)
    case H2 => JsNumber(2)
    case H3 => JsNumber(3)
    case H4 => JsNumber(4)
    case H5 => JsNumber(5)
    case H6 => JsNumber(6)
  }
}
