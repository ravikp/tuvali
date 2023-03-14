package io.mosip.tuvali.verifier.characteristics

import io.mosip.tuvali.transfer.CRCValidator
import io.mosip.tuvali.verifier.GattService
import io.mosip.tuvali.verifier.exception.DataCorruptionException
import java.util.*

private const val NONCE_DATA_SIZE = 12
private const val PUBLIC_KEY_DATA_SIZE = 32
private const val CRC_VALUE_DATA_SIZE = 2 //TODO: remove this from here

class IdentifyRequestCharacteristic(val data: ByteArray) {
  val nonce: ByteArray = data.copyOfRange(0, NONCE_DATA_SIZE)
  val publicKey = data.copyOfRange(NONCE_DATA_SIZE, NONCE_DATA_SIZE + PUBLIC_KEY_DATA_SIZE)

  companion object {
    val uuid: UUID = GattService.IDENTIFY_REQUEST_CHAR_UUID
  }

  init {
    if (data.size < NONCE_DATA_SIZE + PUBLIC_KEY_DATA_SIZE + CRC_VALUE_DATA_SIZE) {
      throw DataCorruptionException("Received data is less than the expected size on ${javaClass.simpleName}")
    }
    CRCValidator.validateCrcSentByWallet(data, javaClass.simpleName)
  }






}
