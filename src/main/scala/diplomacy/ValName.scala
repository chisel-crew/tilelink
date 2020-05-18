// See LICENSE.SiFive for license details.

package tetralink.diplomacy

import scala.language.experimental.macros
import tetralink.macros.ValNameImpl

case class ValName(name: String)

object ValName {
  implicit def materialize(implicit x: ValNameImpl): ValName = ValName(x.name)
}
