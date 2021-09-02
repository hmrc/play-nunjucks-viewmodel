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

import play.api.libs.json.{Json, Reads}
import play.api.mvc.RequestHeader

package object controllers {

  def getFromSession[A: Reads](key: String)(implicit request: RequestHeader): Option[A] =
    request.session.get(key).flatMap {
      value =>
        Json.fromJson[A](Json.parse(value)).asOpt
    }
}
