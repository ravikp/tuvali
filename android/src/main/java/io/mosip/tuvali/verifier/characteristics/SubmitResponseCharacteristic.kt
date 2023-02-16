package io.mosip.tuvali.verifier.characteristics

import android.util.Log
import io.mosip.tuvali.transfer.CheckValue
import io.mosip.tuvali.transfer.Util
import io.mosip.tuvali.verifier.GattService
import io.mosip.tuvali.verifier.exception.CrcCheckFailedException
import java.util.*

class SubmitResponseCharacteristic(val data: ByteArray) {
  private val logTag = javaClass.simpleName
  private val crcValueReceived = Util.twoBytesToIntBigEndian(data.takeLast(2).toByteArray()).toUShort()
  private val seqNumberInMeta = Util.twoBytesToIntBigEndian(data.copyOfRange(0, 2))

  companion object {
    val uuid: UUID = GattService.RESPONSE_SIZE_CHAR_UUID
  }

  init{
    if (!CheckValue.verify(data.drop(2).dropLast(2).toByteArray(), crcValueReceived)) {
      Log.e(logTag, "CRC check failed. Received CRC: $crcValueReceived")
      throw CrcCheckFailedException()
    }
    Log.i(logTag, "CRC check for ${javaClass.simpleName} passed. Received CRC: $crcValueReceived")
    Log.d(logTag, "Received add chunk received chunkSize: ${data.size}, seqNumberInMeta: $seqNumberInMeta")

  }

}
