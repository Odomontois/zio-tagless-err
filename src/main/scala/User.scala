import scalaz.zio.console.Console

final case class User(login: String, pass: String)

final case class Signup(
    loginConfig: LoginConfig,
    passwordConfig: PasswordConfig
) extends HasLoginConfig with HasPasswordConfig with Console.Live
