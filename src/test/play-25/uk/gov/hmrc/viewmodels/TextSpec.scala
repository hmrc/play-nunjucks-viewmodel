package uk.gov.hmrc.viewmodels

import org.scalatest.{FreeSpec, MustMatchers}
import play.api.i18n.Messages
import uk.gov.hmrc.testutils.stubs.MessagesStub

class TextSpec extends FreeSpec with MustMatchers {

  private implicit val messages: Messages = new MessagesStub(Map(
    "basic"      -> "lorem ipsum dolor sit amet",
    "substitute" -> "Hello, {0}!",
    "name"       -> "Bar"
  ))

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
