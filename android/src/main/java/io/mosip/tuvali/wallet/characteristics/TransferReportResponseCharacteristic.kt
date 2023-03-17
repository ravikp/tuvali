package io.mosip.tuvali.wallet.characteristics

import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.transfer.TransferReport
import io.mosip.tuvali.verifier.GattService
import java.util.*

class TransferReportResponseCharacteristic(val data: ByteArray) {
  val transferReport =  TransferReport(data.dropLast(2).toByteArray())

  companion object{
    val uuid: UUID = GattService.TRANSFER_REPORT_RESPONSE_CHAR_UUID
  }

  init{
    CRCValidator.validateDataFromVerifier(data, uuid.toString())
  }
}
