package io.mosip.tuvali.verifier.exception

class CrcValidationFailedException(characteristic: String): VerifierException("CRC validation failed for $characteristic"){

}

