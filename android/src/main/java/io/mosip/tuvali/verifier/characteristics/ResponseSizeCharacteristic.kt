package io.mosip.tuvali.verifier.characteristics

import io.mosip.tuvali.transfer.ByteCount
import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.transfer.Util
import io.mosip.tuvali.verifier.GattService
import java.util.*

class ResponseSizeCharacteristic(val data: ByteArray) {
  val responseSize = Util.networkOrderedByteArrayToInt(data, ByteCount.FourBytes)

  companion object {
    val uuid: UUID = GattService.RESPONSE_SIZE_CHAR_UUID
  }

  init{
    CRCValidator.validateDataFromWallet(data, uuid.toString())
  }
}
