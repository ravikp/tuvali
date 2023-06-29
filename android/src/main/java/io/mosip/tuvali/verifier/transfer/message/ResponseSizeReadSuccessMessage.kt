package io.mosip.tuvali.verifier.transfer.message

class ResponseSizeReadSuccessMessage(val responseSize: Int, val maxChunkSize: Int): IMessage(TransferMessageTypes.RESPONSE_SIZE_READ) {}
