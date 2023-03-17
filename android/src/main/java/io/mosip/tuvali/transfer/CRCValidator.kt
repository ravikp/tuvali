package io.mosip.tuvali.transfer

import com.github.snksoft.crc.CRC
import io.mosip.tuvali.verifier.exception.WalletDataReceivedCrcFailedException
import io.mosip.tuvali.wallet.exception.VerifierDataReceivedCrcFailedException


object CRCValidator {
  //CRC-16/Kermit: https://reveng.sourceforge.io/crc-catalogue/16.htm#crc.cat.crc-16-kermit
  //width=16 poly=0x1021 init=0x0000 refin=true refout=true xorout=0x0000 check=0x2189 residue=0x0000 name="CRC-16/KERMIT"
  //TODO: Need to identify what is check, and residue in the Kermit algorithm
  private val crc16KermitParameters = CRC.Parameters(16, 0x1021, 0x0000, true, true, 0x0000)
  private val CRC_VALUE_DATA_SIZE = crc16KermitParameters.width / 8

  fun calculate(data: ByteArray): UShort {
    return CRC.calculateCRC(crc16KermitParameters, data).toUShort()
  }

  fun getCrcSizeInBytes(): Int {
    return CRC_VALUE_DATA_SIZE
  }

  private fun verify(
    data: ByteArray,
    exceptionFn: (UShort, UShort) -> Nothing,
  ): Boolean {
    val receivedCRC = Util.twoBytesToIntBigEndian(data.takeLast(CRC_VALUE_DATA_SIZE).toByteArray()).toUShort()
    val chunkData = data.dropLast(CRC_VALUE_DATA_SIZE).toByteArray()
    val calculatedCRC = calculate(chunkData)
    if (calculatedCRC != receivedCRC) {
      exceptionFn(receivedCRC, calculatedCRC)
    }
    return true
  }

  fun validateDataFromWallet(data: ByteArray, charUUID: String) {
    verify(data) { receivedCrc: UShort, calculatedCrc: UShort ->
      throw WalletDataReceivedCrcFailedException(charUUID, receivedCrc, calculatedCrc)
    }
  }

  fun validateDataFromVerifier(data: ByteArray, charUUID: String) {
    verify(data) { receivedCrc: UShort, calculatedCrc: UShort ->
      throw VerifierDataReceivedCrcFailedException(charUUID,receivedCrc, calculatedCrc)
    }
  }
}
