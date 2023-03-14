package io.mosip.tuvali.verifier.characteristics

import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.verifier.GattService
import java.util.*

class TransferReportRequestCharacteristic(val data: ByteArray) {
  val receivedReportType = data[0].toInt()

  companion object{
    val uuid: UUID = GattService.TRANSFER_REPORT_REQUEST_CHAR_UUID
  }

  init{
    CRCValidator.validateCrcSentByWallet(data, javaClass.simpleName)
  }
}
