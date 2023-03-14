package io.mosip.tuvali.transfer

import android.util.Log
import io.mosip.tuvali.transfer.Util.Companion.twoBytesToIntBigEndian
import io.mosip.tuvali.verifier.exception.CorruptedChunkReceivedException
import io.mosip.tuvali.transfer.Util.Companion.getLogTag

class Assembler(private val totalSize: Int, private val maxDataBytes: Int ): ChunkerBase(maxDataBytes) {
  private val logTag = getLogTag(javaClass.simpleName)
  private var data: ByteArray = ByteArray(totalSize)
  private var lastReadSeqNumber: Int? = null
  val totalChunkCount = getTotalChunkCount(totalSize)
  private var chunkReceivedMarker = ByteArray(totalChunkCount.toInt())
  private val chunkReceivedMarkerByte: Byte = 1

  init {
    Log.i(logTag, "expected total chunk size: $totalSize")
    if (totalSize == 0) {
      throw CorruptedChunkReceivedException(0, 0, 0)
    }
  }

  fun addChunk(chunkData: ByteArray): Int {
    if (chunkData.size < chunkMetaSize) {
      Log.e(logTag, "received invalid chunk chunkSize: ${chunkData.size}, lastReadSeqNumber: $lastReadSeqNumber")
      return 0
    }
    val seqNumberInMeta = twoBytesToIntBigEndian(chunkData.copyOfRange(0, 2))
    val seqIndexInMeta = seqNumberInMeta - 1

    if (chunkSizeGreaterThanMaxDataBytes(chunkData)) {
      Log.e(logTag, "chunkSizeGreaterThanMaxDataBytes chunkSize: ${chunkData.size}, seqNumberInMeta: $seqNumberInMeta")
      return seqNumberInMeta
    }
    lastReadSeqNumber = seqNumberInMeta
    System.arraycopy(chunkData, 2, data, seqIndexInMeta * effectivePayloadSize, (chunkData.size-chunkMetaSize))
    chunkReceivedMarker[seqIndexInMeta] = chunkReceivedMarkerByte
    //Log.d(logTag, "adding chunk complete at index(0-based): ${seqNumberInMeta}, received chunkSize: ${chunkData.size}")
    return seqNumberInMeta
  }



  private fun chunkSizeGreaterThanMaxDataBytes(chunkData: ByteArray) = chunkData.size > maxDataBytes

  fun isComplete(): Boolean {
    if(chunkReceivedMarker.none { it != chunkReceivedMarkerByte }) {
      return true
    }
    return false
  }

  fun getMissedSequenceNumbers(): IntArray {
    var missedSeqNumbers = intArrayOf()
    chunkReceivedMarker.forEachIndexed() { i, elem ->
      if (elem != chunkReceivedMarkerByte) {
        // Transfer Report requires sequence numbers in 1-based index
        val currentSeqNumber = i + 1
        missedSeqNumbers = missedSeqNumbers + currentSeqNumber
      }
    }
    return missedSeqNumbers
  }

  fun data(): ByteArray {
    return data
  }
}
