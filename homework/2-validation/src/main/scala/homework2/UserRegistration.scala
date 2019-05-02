package homework2

case class RegistrationForm(name: String,
                            email: String,
                            password: String,
                            passwordConfirmation: String,
                            birthYear: String,
                            birthMonth: String,
                            birthDay: String,
                            postalCode: String)

sealed trait RegistrationFormError

case object NameIsEmpty extends RegistrationFormError

case class InvalidEmail(email: String) extends RegistrationFormError

case object PasswordTooShort extends RegistrationFormError
case object PasswordRequiresGreaterSymbolVariety extends RegistrationFormError
case object PasswordsDoNotMatch extends RegistrationFormError

case class InvalidBirthdayDate(dateErrors: Chain[DateError]) extends RegistrationFormError
case class BirthdayDateIsInTheFuture(date: Date) extends RegistrationFormError

case class InvalidPostalCode(code: String) extends RegistrationFormError

sealed trait DateError
case class YearIsNotAnInteger(year: String) extends DateError
case class MonthIsNotAnInteger(month: String) extends DateError
case class DayIsNotAnInteger(day: String) extends DateError
case class MonthOutOfRange(month: Int) extends DateError
case class DayOutOfRange(day: Int) extends DateError
case class InvalidDate(date: Date) extends DateError

case class Email(user: String, domain: String)

case class User(name: String,
                email: Email,
                passwordHash: String,
                birthday: Date,
                postalCode: Option[String])

object UserRegistration {
  def registerUser(userCountryPostalCodeVerifier: String => Boolean, today: Date)
                  (form: RegistrationForm): Validated[RegistrationFormError, User] = ???
}
