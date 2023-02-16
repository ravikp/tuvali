package io.mosip.tuvali.verifier.characteristics

import android.util.Log
import io.mosip.tuvali.transfer.CheckValue
import io.mosip.tuvali.transfer.Util
import io.mosip.tuvali.verifier.GattService
import io.mosip.tuvali.verifier.exception.CrcCheckFailedException
import java.util.*

class ResponseSizeCharacteristic(val data: ByteArray) {
  private val logTag = javaClass.simpleName
  //TODO: remove this after formatting response size (474)
  private val responseSizeByteArray = data.dropLast(2).toByteArray()
  val responseSize = String(responseSizeByteArray).toInt()
  private val crcValueReceived = Util.twoBytesToIntBigEndian(data.takeLast(2).toByteArray()).toUShort()



  companion object {
    val uuid: UUID = GattService.RESPONSE_SIZE_CHAR_UUID
  }

  init{
    if (!CheckValue.verify(responseSizeByteArray, crcValueReceived)) {
      Log.e(logTag, "CRC check failed. Received CRC: $crcValueReceived")
      throw CrcCheckFailedException()
    }
    Log.i(logTag, "CRC check for ${javaClass.simpleName} passed. Received CRC: $crcValueReceived")
    Log.d(logTag, "Received response size on characteristic value: $responseSize")
  }
}
