package io.mosip.tuvali.transfer

import android.util.Log
import io.mosip.tuvali.transfer.ByteCount.TwoBytes
import io.mosip.tuvali.transfer.Util.Companion.intToNetworkOrderedByteArray
import io.mosip.tuvali.transfer.Util.Companion.getLogTag

class Chunker(private val data: ByteArray, private val maxChunkDataBytes: Int) :
  ChunkerBase(maxChunkDataBytes) {
  private val logTag = getLogTag(javaClass.simpleName)
  private var chunksReadCounter: Int = 0
  private val lastChunkByteCount = getLastChunkByteCount(data.size)
  private val totalChunkCount = getTotalChunkCount(data.size).toInt()
  private val preSlicedChunks: Array<ByteArray?> = Array(totalChunkCount) { null }

  init {
    Log.i(logTag, "Total number of chunks calculated: $totalChunkCount")
    for (idx in 0 until totalChunkCount) {
      preSlicedChunks[idx] = chunk(idx)
    }
  }

  fun next(): ByteArray {
    return preSlicedChunks[chunksReadCounter++]!!
  }

  fun chunkBySequenceNumber(missedSeqNumber: ChunkSeqNumber): ByteArray {
    return preSlicedChunks[missedSeqNumber.toSeqIndex()]!!
  }

  private fun chunk(seqIndex: ChunkSeqIndex): ByteArray {
    val fromIndex = seqIndex * effectivePayloadSize
    return if (isLastChunkSmallerSize(seqIndex)) {
      frameChunk(seqIndex.toSeqNumber(), fromIndex, fromIndex + lastChunkByteCount)
    } else {
      val toIndex = fromIndex + effectivePayloadSize
      frameChunk(seqIndex.toSeqNumber(), fromIndex, toIndex)
    }
  }

  private fun isLastChunkSmallerSize(seqIndex: Int) =
    isLastChunkIndex(seqIndex) && lastChunkByteCount > 0

  private fun isLastChunkIndex(seqIndex: Int) = seqIndex == (totalChunkCount - 1)

  /*
      <----------------------------------- MaxChunkDataBytes ------------------------------------->
      + --------------------- + ----------------------------------- + --------------------------- +
      |                       |                                     |                             |
      |  chunk sequence no    |        chunk payload                |   checksum value of data    |
      |      (2 bytes)        |   (upto MaxChunkDataBytes-4 bytes)  |        ( 2 bytes)           |
      |                       |                                     |                             |
      + --------------------- + ----------------------------------- + --------------------------- +
  */
  private fun frameChunk(seqNumber: Int, fromIndex: Int, toIndex: Int): ByteArray {
    val dataChunk = intToNetworkOrderedByteArray(seqNumber, TwoBytes) + data.copyOfRange(fromIndex, toIndex)
    val crc = CRCValidator.calculate(dataChunk)

    return dataChunk + intToNetworkOrderedByteArray(crc.toInt(), TwoBytes)

  }

  fun isComplete(): Boolean {
    Log.i(logTag,"chunksReadCounter: $chunksReadCounter")
    val isComplete = chunksReadCounter >= totalChunkCount
    if (isComplete) {
      Log.d(logTag, "isComplete: true, totalChunks: $totalChunkCount , chunkReadCounter(1-indexed): $chunksReadCounter")
    }
    return isComplete
  }
}
