package forms

import java.time.LocalDate

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class DateOfBirthFormProvider @Inject() extends Mappings {

  def apply(): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "dateOfBirth.error.invalid",
        allRequiredKey = "dateOfBirth.error.required.all",
        twoRequiredKey = "dateOfBirth.error.required.two",
        requiredKey    = "dateOfBirth.error.required"
      )
    )
}
