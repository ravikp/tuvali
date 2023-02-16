package io.mosip.tuvali.wallet.characteristics

import android.util.Log
import io.mosip.tuvali.transfer.CheckValue
import io.mosip.tuvali.transfer.Util
import io.mosip.tuvali.verifier.GattService
import io.mosip.tuvali.verifier.exception.CrcCheckFailedException
import java.util.*

class DisconnectCharacteristic(val data: ByteArray) {
  private val logTag = javaClass.simpleName
  val status = data[0].toInt()
  private val crcValueReceived = Util.twoBytesToIntBigEndian(data.takeLast(2).toByteArray()).toUShort()

  companion object{
    val uuid: UUID = GattService.DISCONNECT_CHAR_UUID
  }

  init{
    if (!CheckValue.verify(data.dropLast(2).toByteArray(), crcValueReceived)) {
      Log.e(logTag, "CRC check failed. Received CRC: $crcValueReceived")
      throw CrcCheckFailedException()
    }
    Log.i(logTag, "CRC check for ${javaClass.simpleName} passed. Received CRC: $crcValueReceived")
  }
}

