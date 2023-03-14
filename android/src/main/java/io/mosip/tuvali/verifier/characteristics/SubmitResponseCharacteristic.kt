package io.mosip.tuvali.verifier.characteristics

import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.verifier.GattService
import java.util.*

class SubmitResponseCharacteristic(val data: ByteArray) {
  val responseData = data.dropLast(2).toByteArray()

  companion object {
    val uuid: UUID = GattService.SUBMIT_RESPONSE_CHAR_UUID
  }

  init{
   CRCValidator.validateCrcSentByWallet(data, javaClass.simpleName)

  }

}
