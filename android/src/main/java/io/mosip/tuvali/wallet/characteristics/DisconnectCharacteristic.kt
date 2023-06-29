package io.mosip.tuvali.wallet.characteristics

import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.verifier.GattService
import java.util.*

class DisconnectCharacteristic(val data: ByteArray) {
  val status = data[0].toInt()

  companion object{
    val uuid: UUID = GattService.DISCONNECT_CHAR_UUID
  }

  init{
    CRCValidator.validateDataFromVerifier(data, uuid.toString())
  }
}

