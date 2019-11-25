package uk.gov.hmrc.testutils.stubs

import java.text.MessageFormat

import play.api.i18n.Messages


class MessagesStub(private val messageMap: Map[String, String] = Map()) extends Messages(null, null) {

  override def apply(key: String, args: Any*): String = {
    val msg: String = messageMap.getOrElse(key, key)
    val javaArgs: Array[String] = args.toArray.map(_.toString)
    new MessageFormat(msg).format(javaArgs)
  }
}
