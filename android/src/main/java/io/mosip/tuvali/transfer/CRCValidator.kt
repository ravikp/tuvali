package io.mosip.tuvali.transfer

import android.util.Log
import com.github.snksoft.crc.CRC
import io.mosip.tuvali.transfer.Util.Companion.getLogTag


object CRCValidator {
  private val logTag = getLogTag(javaClass.simpleName)

  //CRC-16/Kermit: https://reveng.sourceforge.io/crc-catalogue/16.htm#crc.cat.crc-16-kermit
  //width=16 poly=0x1021 init=0x0000 refin=true refout=true xorout=0x0000 check=0x2189 residue=0x0000 name="CRC-16/KERMIT"
  //TODO: Need to identify what is check, and residue in the Kermit algorithm
  private val crc16KermitParameters = CRC.Parameters(16, 0x1021, 0x0000, true, true, 0x0000)
  private val CRC_VALUE_DATA_SIZE = crc16KermitParameters.width/8

  fun calculate(data: ByteArray): UShort {
    return CRC.calculateCRC(crc16KermitParameters, data).toUShort()
  }

  fun getDataSize(size: Int): Int {
    return size - CRC_VALUE_DATA_SIZE
  }

  private fun verify(data: ByteArray, characteristic: String) : Boolean {
    val receivedCRC = Util.twoBytesToIntBigEndian(data.takeLast(CRC_VALUE_DATA_SIZE).toByteArray()).toUShort()
    val chunkData = data.dropLast(2).toByteArray()
    val calculatedCRC = calculate(chunkData)
    if(calculatedCRC != receivedCRC) {
      Log.e(logTag, "CRC check failed for $characteristic. Received CRC: $receivedCRC, Calculated CRC: $calculatedCRC")
      return false
    }
    return true
  }

  fun validateCrcSentByWallet(data: ByteArray, characteristic: String): Boolean{
    if(!verify(data, characteristic))
      throw io.mosip.tuvali.verifier.exception.CrcValidationFailedException(characteristic)
    return true
  }

  fun validateCrcSentByVerifier(data: ByteArray, characteristic: String): Boolean{
    if(!verify(data, characteristic))
      throw io.mosip.tuvali.wallet.exception.CrcValidationFailedException(characteristic)
    return true
  }
}
