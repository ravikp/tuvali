package io.mosip.tuvali.verifier.exception

class WalletDataReceivedCrcFailedException(
  charUUID: String,
  receivedCRC: UShort,
  calculatedCRC: UShort
) : VerifierException("CRC check failed for characteristic: $charUUID. ReceivedCRC: $receivedCRC, CalculatedCRC: $calculatedCRC")
