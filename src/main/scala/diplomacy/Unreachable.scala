// See LICENSE.SiFive for license details.

package tetralink.diplomacy

case object Unreachable {
  def apply(): Nothing = throw new AssertionError("unreachable code")
}
