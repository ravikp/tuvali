package io.mosip.tuvali.verifier.exception

import io.mosip.tuvali.exception.BLEException
import io.mosip.tuvali.exception.ErrorCode

class WalletDataReceivedCrcFailedException(
  charUUID: String,
  receivedCRC: UShort,
  calculatedCRC: UShort
) :
  BLEException(
    "CRC check failed for characteristic: $charUUID. ReceivedCRC: $receivedCRC, CalculatedCRC: $calculatedCRC",
    null,
    ErrorCode.WalletDataReceivedCrcFailedException
  )
