import scalaz.zio.ZIO

object Validations {
  type HasSignupConfig = HasLoginConfig with HasPasswordConfig

  def validateLogin(login: String): ZIO[HasLoginConfig, Capture[LoginErr], Unit] =
    ZIO.accessM { r: LoginConfig =>
      ZIO.fail(LoginErr.tooLong(r.maxLoginLength)).when(login.length > r.maxLoginLength) *>
        ZIO.fail(LoginErr.badFormat).when(!login.matches(r.regex))
    }.provideSome(_.loginConfig)

  def validatePassword(pass: String): ZIO[HasPasswordConfig, Capture[PasswordErr], Unit] =
    ZIO.accessM { r: PasswordConfig =>
      ZIO.fail(PasswordErr.tooShort(r.minLength)).when(pass.length < r.minLength) <*
        ZIO.foreach(r.groups) {
          case (name, re) => ZIO.fail(PasswordErr.doesNotContain(name)).when(re.findFirstMatchIn(pass).isEmpty)
        }
    }.provideSome(_.passwordConfig)

  def validateUser(user: User) = validateLogin(user.login) *> validatePassword(user.pass)
}
