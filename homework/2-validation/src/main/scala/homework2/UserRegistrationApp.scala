package homework2

import homework2.io.Console._
import homework2.io.IO

object UserRegistrationApp {
  val validPostalCodes: String => Boolean = Set("1000", "1164", "9000")
  val today = Date(2019, 5, 4)
  val register = UserRegistration.registerUser(validPostalCodes, today)(_)

  val registrationInput: IO[RegistrationForm] = for {
    name <- promptInput("Enter your name:")
    email <- promptInput("Enter your email:")
    password <- promptInput("Enter your password:")
    passwordConfirmation <- promptInput("Confirm your password:")
    date <- dateInput("Enter your birthday:")
    (year, month, day) = date
    postalCode <- promptInput("Enter your postal code (optional):")
  } yield RegistrationForm(name, email, password, passwordConfirmation, year, month, day, postalCode)

  def promptInput(prompt: String): IO[String] = for {
    _ <- putStrLn(prompt)
    input <- getStrLn
  } yield input

  def dateInput(prompt: String): IO[(String, String, String)] = for {
    _ <- putStrLn(prompt)
    year <- promptInput("year:")
    month <- promptInput("month:")
    day <- promptInput("day:")
  } yield(year, month, day)

  def registrationOutput(userValidation: Validated[RegistrationFormError, User]): IO[Unit] = userValidation match {
    case Valid(user) => for {
      _ <- putStrLn("User registered successfully:")
      _ <- putStrLn(user.toString)
    } yield ()

    case Invalid(errors) =>
      errors
        .map(errorToDescription)
        .map(putStrLn)
        .foldLeft {
          putStrLn("The following errors have been found:")
        } {
          (acc, next) => acc.flatMap(_ => next)
        }
  }

  def errorToDescription(error: RegistrationFormError): String = error match {
    case NameIsEmpty => "Name is empty"
    case InvalidEmail(email) => s"Provided email $email is not valid"
    case PasswordTooShort => "Password is too short. It should be at least 8 symbols long"
    case PasswordRequiresGreaterSymbolVariety => "Password should contain at least one letter, digit and a special symbol"
    case PasswordsDoNotMatch => "Passwords do not match"
    case InvalidBirthdayDate(dateErrors) =>
      val errors = dateErrors.map {
        case YearIsNotAnInteger(year) => s"Day $year is not an integer"
        case MonthIsNotAnInteger(month) => s"Day $month is not an integer"
        case DayIsNotAnInteger(day) => s"Day $day is not an integer"

        case MonthOutOfRange(month) => s"Month $month is out of range"
        case DayOutOfRange(day) => s"Day $day is out of range"

        case InvalidDate(date) => s"Date $date is not a valid date"
      }.toList

      ("Provided birthday date is invalid:" :: errors).mkString("\n")
    case BirthdayDateIsInTheFuture(date) => s"Provided birthday date '$date' is in the future"
    case InvalidPostalCode(code) => s"Provided postal code '$code' is invalid"
  }

  def main(args: Array[String]): Unit = {
    registrationInput
      .map(register)
      .flatMap(registrationOutput)
      .unsafeRun()
  }
}
