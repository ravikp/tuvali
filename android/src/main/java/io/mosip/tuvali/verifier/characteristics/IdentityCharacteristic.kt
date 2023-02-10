package io.mosip.tuvali.verifier.characteristics

import android.util.Log
import io.mosip.tuvali.transfer.CheckValue
import io.mosip.tuvali.transfer.Util
import io.mosip.tuvali.verifier.GattService
import io.mosip.tuvali.verifier.exception.CrcCheckFailedException
import java.util.UUID

class IdentityCharacteristic(val data: ByteArray) {
  private val logTag = "IdentityCharacteristic"

  companion object{
    val uuid : UUID = GattService.IDENTITY_CHARACTERISTIC_UUID
  }
  init{
    val iv = data.copyOfRange(0, 12)
    val walletPubKey = data.copyOfRange(12, 12 + 32)
    val crcValueReceived = Util.twoBytesToIntBigEndian(data.copyOfRange(44,46)).toUShort()
    if(!CheckValue.verify(iv+walletPubKey, crcValueReceived)){
      throw CrcCheckFailedException()
      //TODO: handling
    }
  }

}
