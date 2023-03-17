package io.mosip.tuvali.verifier.characteristics

import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.verifier.GattService
import java.util.*

class ResponseSizeCharacteristic(val data: ByteArray) {
  //TODO: refactor this after formatting response size (474)
  val responseSize = String(data.dropLast(2).toByteArray()).toInt()

  companion object {
    val uuid: UUID = GattService.RESPONSE_SIZE_CHAR_UUID
  }

  init{
    CRCValidator.validateDataFromWallet(data, uuid.toString())
  }
}
