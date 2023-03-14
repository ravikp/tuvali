package io.mosip.tuvali.wallet.characteristics

import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.verifier.GattService
import java.util.*

class VerificationStatusCharacteristic(val data: ByteArray) {
  val status = data[0].toInt()


  companion object{
    val uuid: UUID = GattService.VERIFICATION_STATUS_CHAR_UUID
  }

  init{
    CRCValidator.validateCrcSentByVerifier(data, javaClass.simpleName)
  }
}
