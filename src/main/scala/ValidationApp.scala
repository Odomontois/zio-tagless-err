import Validations.HasSignupConfig
import scalaz.zio._
import scalaz.zio.console._

import scala.util.matching.Regex
import scala.reflect.runtime.universe._

object ValidationApp extends App {
  def typestr[A: WeakTypeTag](x: => A): String = weakTypeOf[A].dealias.widen.toString

  def validateUserString(user: User): ZIO[HasSignupConfig, Nothing, String] =
    Validations
      .validateUser(user)
      .const("succeded")
      .catchAll(c =>
        ZIO.succeed(c.continue(new LoginErr[String] with PasswordErr[String] {
          def tooLong(maxLength: Int): String       = s"login should not be longer than $maxLength"
          def badFormat: String                     = s"login is unacceptable"
          def tooShort(required: Int): String       = s"password is too short, required: $required"
          def doesNotContain(chars: String): String = s"password should contain one $chars"
        })))

  val signup = Signup(
    LoginConfig(
      maxLoginLength = 10,
      regex = "\\w+"
    ),
    PasswordConfig(
      minLength = 10,
      groups = List(
        "digit"             -> "\\d".r,
        "letter"            -> "\\w".r,
        "special character" -> "@#$%^&*():;'<>".mkString("[\\", "\\", "]").r
      )
    )
  )

  val users = List(User("oleg", "sdfsdfsdf"),
                   User("lolcat", "sdfdf123123"),
                   User("fedot kamil", "sdfsdfsdfsdfsdfss##11"),
                   User("yan", "dfgfdgfg123123(*(&"))

  def run(args: List[String]): ZIO[ValidationApp.Environment, Nothing, Int] =
    putStr(typestr(Validations.validateUser(null))) *>
      ZIO.foreach(users)(user => validateUserString(user) >>= (res => putStrLn(s"$user : $res"))).provide(signup).const(0)

}
