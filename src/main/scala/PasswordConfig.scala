import scala.util.matching.Regex

final case class PasswordConfig(minLength: Int, groups: List[(String, Regex)])

trait HasPasswordConfig {
  def passwordConfig: PasswordConfig
}
