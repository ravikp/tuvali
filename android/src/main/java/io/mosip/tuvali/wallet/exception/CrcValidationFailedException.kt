package io.mosip.tuvali.wallet.exception


class CrcValidationFailedException(characteristic: String): WalletException("CRC validation failed for $characteristic") {}

