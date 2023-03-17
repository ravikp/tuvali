package io.mosip.tuvali.wallet.exception

class VerifierDataReceivedCrcFailedException(
  charUUID: String,
  receivedCRC: UShort,
  calculatedCRC: UShort
) : WalletException("CRC check failed for characteristic: $charUUID. ReceivedCRC: $receivedCRC, CalculatedCRC: $calculatedCRC")
