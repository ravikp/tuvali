package io.mosip.tuvali.verifier.exception

class CorruptedChunkReceivedException(size: Int, receivedSeqIndex: Int, receivedMtuSize: Int) : Throwable(
  "size: $size, receivedSeqIndex: $receivedSeqIndex, receivedMtuSize: $receivedMtuSize"
) {}
