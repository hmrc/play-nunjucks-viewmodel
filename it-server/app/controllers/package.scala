import play.api.libs.json.{Json, Reads}
import play.api.mvc.RequestHeader

package object controllers {

  def getFromSession[A: Reads](key: String)(implicit request: RequestHeader): Option[A] =
    request.session.get(key).flatMap {
      value =>
        Json.fromJson[A](Json.parse(value)).asOpt
    }
}
