// See LICENSE.SiFive for license details.

package tetralink.diplomacy

import Chisel._
import tetralink.config.Parameters
import tetralink.diplomaticobjectmodel.DiplomaticObjectModelAddressing
import tetralink.diplomaticobjectmodel.model._
import tetralink.util.DescribedSRAM

abstract class DiplomaticSRAM(
  address: AddressSet,
  beatBytes: Int,
  devName: Option[String],
  dtsCompat: Option[Seq[String]] = None
)(implicit p: Parameters)
    extends LazyModule {
  val device = devName
    .map(new SimpleDevice(_, dtsCompat.getOrElse(Seq("sifive,sram0"))))
    .getOrElse(new MemoryDevice())

  def getOMMemRegions(resourceBindings: ResourceBindings): Seq[OMMemoryRegion] =
    DiplomaticObjectModelAddressing.getOMMemoryRegions(devName.getOrElse(""), resourceBindings) // TODO name source???

  val resources = device.reg("mem")

  def bigBits(x: BigInt, tail: List[Boolean] = Nil): List[Boolean] =
    if (x == 0) tail.reverse else bigBits(x >> 1, ((x & 1) == 1) :: tail)

  def mask: List[Boolean] = bigBits(address.mask >> log2Ceil(beatBytes))

  // Use single-ported memory with byte-write enable
  def makeSinglePortedByteWriteSeqMem(size: BigInt, lanes: Int = beatBytes, bits: Int = 8) = {
    // We require the address range to include an entire beat (for the write mask)

    val (mem, omSRAM) = DescribedSRAM(
      name = devName.getOrElse("mem"),
      desc = devName.getOrElse("mem"),
      size = size,
      data = Vec(lanes, UInt(width = bits))
    )
    devName.foreach(n => mem.suggestName(n.split("-").last))

    val omMem: OMMemory = DiplomaticObjectModelAddressing.makeOMMemory(
      desc = "mem", //lim._2.name.map(n => n).getOrElse(lim._1.name),
      depth = size,
      data = Vec(lanes, UInt(width = bits))
    )

    (mem, omSRAM, Seq(omMem))
  }
}
