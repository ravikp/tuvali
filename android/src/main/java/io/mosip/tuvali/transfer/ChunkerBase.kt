package io.mosip.tuvali.transfer

import kotlin.math.ceil

const val DEFAULT_CHUNK_SIZE = 23

open class ChunkerBase(mtuSize: Int = DEFAULT_CHUNK_SIZE) {
  private val seqNumberReservedByteSize = 2
  private val mtuReservedByteSize = 2
  val effectiveMTUSize = mtuSize - 5
  val chunkMetaSize = seqNumberReservedByteSize + mtuReservedByteSize
  val effectivePayloadSize = effectiveMTUSize - chunkMetaSize

  fun getTotalChunkCount(dataSize: Int): Double {
    return ceil((dataSize.toDouble()/effectivePayloadSize.toDouble()))
  }

  fun getLastChunkByteCount(dataSize: Int): Int {
    return dataSize % effectivePayloadSize
  }
}
