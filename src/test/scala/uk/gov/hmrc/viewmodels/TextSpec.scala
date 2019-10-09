package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.i18n.{Messages, MessagesApi}
import play.api.test.Helpers

class TextSpec extends FreeSpec with MustMatchers {

  val messagesApi: MessagesApi = Helpers.stubMessagesApi(Map(
    "en" -> Map(
      "basic"      -> "lorem ipsum dolor sit amet",
      "substitute" -> "Hello, {0}!",
      "name"       -> "Bar"
    )
  ))

  implicit val messages: Messages = Helpers.stubMessages(messagesApi)

  "literal text" - {

    "must resolve to its contents" in {

      val message = Text.Literal("foo")
      message.resolve mustEqual "foo"
    }
  }

  "a computed message" - {

    "must resolve to a value from the messages file" in {

      val message = Text.Message("basic")
      message.resolve mustEqual "lorem ipsum dolor sit amet"
    }

    "must resolve to a value from the messages file with arguments substituted" in {

      val message = Text.Message("substitute", "Foo")
      message.resolve mustEqual "Hello, Foo!"
    }

    "must resolve to a value from messages file with a resolve argument substituted" in {

      val message = Text.Message("substitute", Text.Message("name"))
      message.resolve mustEqual "Hello, Bar!"
    }
  }
}
