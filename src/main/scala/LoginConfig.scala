final case class LoginConfig(maxLoginLength: Int, regex: String)

trait HasLoginConfig {
  def loginConfig: LoginConfig
}