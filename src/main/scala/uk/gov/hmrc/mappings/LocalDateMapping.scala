/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.mappings

import java.time.LocalDate

import cats.implicits._
import play.api.data.format.Formatter
import play.api.data.validation.Constraint
import play.api.data.{FormError, Forms, Mapping}
import uk.gov.hmrc.viewmodels.Text

import scala.util.control.Exception.nonFatalCatch
import scala.util.{Failure, Success, Try}

class LocalDateMapping(
  override val key: String = "",
  override val constraints: Seq[Constraint[LocalDate]] = Seq.empty,
  formatter: Formatter[Int] = LocalDateMapping.intFormatter()
) extends Mapping[LocalDate] {

  private sealed trait DateField {
    def name(prefix: String): String
    def messageKey: String
  }

  private case object Day extends DateField {
    override def name(prefix: String): String = prefix
    override def messageKey: String           = "site.day"
  }

  private case object Month extends DateField {
    override def name(prefix: String): String = s"$prefix.month"
    override def messageKey: String           = "site.month"
  }

  private case object Year extends DateField {
    override def name(prefix: String): String = s"$prefix.year"
    override def messageKey: String           = "site.year"
  }

  private val fieldKeys: Seq[DateField] = List(Day, Month, Year)

  private val dayField   = Forms.of(formatter).withPrefix("day").withPrefix(key)
  private val monthField = Forms.of(formatter).withPrefix("month").withPrefix(key)
  private val yearField  = Forms.of(formatter).withPrefix("year").withPrefix(key)

  override val mappings: Seq[Mapping[_]] =
    Seq(this) ++ dayField.mappings ++ monthField.mappings ++ yearField.mappings

  override def bind(data: Map[String, String]): Either[Seq[FormError], LocalDate] = {

    def bindField(mapping: Mapping[Int]): Either[List[FormError], Int] =
      mapping.bind(data).leftMap(_.toList)

    val fields = fieldKeys.map { field =>
      field -> data.get(s"$key.${field.toString.toLowerCase}").filter(_.nonEmpty)
    }.toMap

    val missingFields = fields
      .withFilter(_._2.isEmpty)
      .map(_._1)
      .toList

    val requiredFields = fields.count(_._2.isDefined) match {
      case 2 =>
        Left(
          List(
            FormError(
              missingFields.head.name(key),
              "date.required.1",
              missingFields.map(f => Text.Message(f.messageKey))
            )
          )
        )
      case 1 =>
        Left(
          List(
            FormError(
              missingFields.head.name(key),
              "date.required.2",
              missingFields.map(f => Text.Message(f.messageKey))
            )
          )
        )
      case 0 =>
        Left(List(FormError(key, "date.required")))
      case _ =>
        Right(())
    }

    val compiledDate = (
      bindField(dayField),
      bindField(monthField),
      bindField(yearField)
    ).parTupled.flatMap { case (day, month, year) =>
      toDate(key, day, month, year)
    }

    (requiredFields &> compiledDate).leftMap(_.headOption.toList)
  }

  override def unbind(value: LocalDate): Map[String, String] =
    dayField.unbind(value.getDayOfMonth) ++
      monthField.unbind(value.getMonthValue) ++
      yearField.unbind(value.getYear)

  override def unbindAndValidate(value: LocalDate): (Map[String, String], Seq[FormError]) = {
    val (dayMap, dayErrors)     = dayField.unbindAndValidate(value.getDayOfMonth)
    val (monthMap, monthErrors) = monthField.unbindAndValidate(value.getMonthValue)
    val (yearMap, yearErrors)   = yearField.unbindAndValidate(value.getYear)
    (dayMap ++ monthMap ++ yearMap, dayErrors ++ monthErrors ++ yearErrors)
  }

  override def withPrefix(prefix: String): Mapping[LocalDate] =
    addPrefix(prefix).fold(this)(new LocalDateMapping(_, constraints, formatter))

  override def verifying(constraints: Constraint[LocalDate]*): Mapping[LocalDate] =
    new LocalDateMapping(key, this.constraints ++ constraints, formatter)

  private def toDate(key: String, day: Int, month: Int, year: Int): Either[List[FormError], LocalDate] =
    Try(LocalDate.of(year, month, day)) match {
      case Success(date) =>
        Right(date)
      case Failure(_)    =>
        Left(List(FormError(key, "date.invalid")))
    }
}

object LocalDateMapping {

  def intFormatter(
    invalidMessageKey: String = "date.invalid"
  ): Formatter[Int] = new Formatter[Int] {

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Int] = {

      val parentKey: Option[String] = {
        val split = key.split("\\.")
        Try(split.init).toOption.map(_.mkString("."))
      }

      data.get(key) match {
        case Some("") | None => Left(List.empty)
        case Some(value)     =>
          nonFatalCatch.either(value.toInt).left.map(_ => List(FormError(parentKey.getOrElse(key), invalidMessageKey)))
      }
    }

    override def unbind(key: String, value: Int): Map[String, String] =
      Map(key -> value.toString)
  }
}
