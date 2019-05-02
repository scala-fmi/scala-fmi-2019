package homework2

import homework2.UserRegistration.registerUser
import org.scalatest.{FlatSpec, Matchers}

class UserRegistrationTest extends FlatSpec with Matchers {
  "An empty form" should "generate errors for the non optional fields" in {
    val emptyForm = RegistrationForm("", "", "", "", "", "", "", "")

    val validation = registerUser(Set.empty, Date(2019, 5, 4))(emptyForm)

    validation.isValid shouldBe false

    val Invalid(errors) = validation
    val errorsSet = errors.toSet
    val birthdayErrors = errorsSet.collectFirst {
      case InvalidBirthdayDate(dateErrors) => dateErrors.toSet
    }

    errorsSet should have size 5

    errorsSet should contain allOf (
      NameIsEmpty,
      InvalidEmail(""),
      PasswordTooShort,
      PasswordRequiresGreaterSymbolVariety
    )

    birthdayErrors shouldEqual Some(Set(
      YearIsNotAnInteger(""),
      MonthIsNotAnInteger(""),
      DayIsNotAnInteger("")
    ))
  }
}
