package io.mosip.tuvali.verifier.characteristics

import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.verifier.GattService
import io.mosip.tuvali.verifier.exception.DataCorruptionException
import java.util.*

private const val NONCE_DATA_SIZE = 12
private const val PUBLIC_KEY_DATA_SIZE = 32

class IdentifyRequestCharacteristic(val data: ByteArray) {
  private val crcValueInBytes = CRCValidator.getCrcSizeInBytes()
  val nonce: ByteArray = data.copyOfRange(0, NONCE_DATA_SIZE)
  val publicKey = data.copyOfRange(NONCE_DATA_SIZE, NONCE_DATA_SIZE + PUBLIC_KEY_DATA_SIZE)

  companion object {
    val uuid: UUID = GattService.IDENTIFY_REQUEST_CHAR_UUID
  }

  init {
    if (data.size < NONCE_DATA_SIZE + PUBLIC_KEY_DATA_SIZE + crcValueInBytes) {
      throw DataCorruptionException("Received data is less than the expected size on ${javaClass.simpleName}. Data size: ${data.size}")
    }
    CRCValidator.validateDataFromWallet(data, uuid.toString())
  }






}
