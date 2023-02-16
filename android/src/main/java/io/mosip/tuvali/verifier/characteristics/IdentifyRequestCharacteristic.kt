package io.mosip.tuvali.verifier.characteristics

import android.util.Log
import io.mosip.tuvali.transfer.CheckValue
import io.mosip.tuvali.transfer.Util
import io.mosip.tuvali.verifier.GattService
import io.mosip.tuvali.verifier.exception.CrcCheckFailedException
import org.bouncycastle.util.encoders.Hex
import java.util.UUID

class IdentifyRequestCharacteristic(val data: ByteArray) {
  private val logTag = javaClass.simpleName
  val iv: ByteArray = data.copyOfRange(0, 12)
  val walletPublicKey = data.copyOfRange(12, 12 + 32)
  private val crcValueReceived = Util.twoBytesToIntBigEndian(data.takeLast(2).toByteArray()).toUShort()



  companion object {
    val uuid: UUID = GattService.IDENTIFY_REQUEST_CHAR_UUID
  }

  init {
    Log.i(
      logTag,
      "Received wallet iv: ${Hex.toHexString(iv)}, " +
        "Wallet public key: ${Hex.toHexString(walletPublicKey)}"
    )
    // Total size of identity char value will be 12 bytes IV + 32 bytes pub key + 2 bytes CRC checksum
    if (data.size < 12 + 32 + 2) {
      // TODO: 497 handling
    }
    if (!CheckValue.verify(iv + walletPublicKey, crcValueReceived)) {
      Log.e(logTag, "CRC check failed. Received CRC: $crcValueReceived")
      throw CrcCheckFailedException()
    }
    Log.i(logTag, "CRC check for ${javaClass.simpleName} passed. Received CRC: $crcValueReceived")
  }






}
