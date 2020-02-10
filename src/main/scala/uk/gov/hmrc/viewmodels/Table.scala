package uk.gov.hmrc.viewmodels

import play.api.i18n.Messages
import play.api.libs.functional.syntax._
import play.api.libs.json._
import uk.gov.hmrc.viewmodels.Table.Cell

final case class Table(head: Seq[Cell],
                       rows: Seq[Seq[Cell]],
                       caption: Option[String] = None,
                       captionClasses: Seq[String] = Seq.empty,
                       firstCellIsHeader: Boolean = false,
                       classes: Seq[String] = Seq.empty,
                       attributes: Map[String, String] = Map.empty)

object Table {

  implicit def writes(implicit messages: Messages): OWrites[Table] = (
    (__ \ "caption").writeNullable[String] and
      (__ \ "captionClasses").writeNullable[String] and
      (__ \ "firstCellIsHeader").write[Boolean] and
      (__ \ "head").writeNullable[Seq[Cell]] and
      (__ \ "rows").write[Seq[Seq[Cell]]] and
      (__ \ "classes").writeNullable[String] and
      (__ \ "attributes").writeNullable[Map[String, String]]
    ) { table =>
    val head = Some(table.head).filter(_.nonEmpty)
    val attributes = Some(table.attributes).filter(_.nonEmpty)
    (table.caption, classes(table.captionClasses), table.firstCellIsHeader,
      head, table.rows, classes(table.classes), attributes)
  }

  final case class Cell(content: Content,
                        classes: Seq[String] = Seq.empty,
                        format: Option[String] = None,
                        colspan: Option[String] = None,
                        rowspan: Option[String] = None,
                        attributes: Map[String, String] = Map.empty
                       ) extends WithContent

  object Cell {

    implicit def writes(implicit messages: Messages): OWrites[Cell] = (
      (__ \ "text").writeNullable[Text] and
        (__ \ "html").writeNullable[Html] and
        (__ \ "classes").writeNullable[String] and
        (__ \ "format").writeNullable[String] and
        (__ \ "colspan").writeNullable[String] and
        (__ \ "rowspan").writeNullable[String] and
        (__ \ "attributes").writeNullable[Map[String, String]]
      ) { cell =>
      val attributes = Some(cell.attributes).filter(_.nonEmpty)
      (cell.text, cell.html, classes(cell.classes), cell.format, cell.colspan, cell.rowspan, attributes)
    }
  }

  private def classes(classes: Seq[String]): Option[String] =
    if (classes.isEmpty) None else Some(classes.mkString(" "))
}