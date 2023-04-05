# Adding Checksum to all Write characteristics

CRC value is appended at the end of the data to be written in each characteristic and consumes 2 bytes. CRC checksum values have been added to the following characteristics :

	- IdentifyRequestCharacteristic
	- ResponseSizeCharacteristic
	- SubmitResponseCharacteristic
	- TransferReportRequestCharacteristic
	- TransferReportResponseCharacteristic
	- VerificationStatusCharacteristic
	- DisconnectCharacteristic

When the wallet writes public key to the `IdentifyRequestCharacteristic` and the CRC checksum fails on the verifier side, the verifier throws an error and disconnects from the wallet.

Before starting the VC transfer, the size of the VC is sent to the verifier on `ResponseSizeCharacteristic`. If the CRC checksum fails on the verifier, it  throws an error and disconnects from the wallet.

Once the VC sharing starts, each chunks of the VC is sent on `SubmitResponseCharacteristic`. If the checksum fails for any chunk, the verifier logs the error and ignores the chunk. It waits for the next chunk to receive and processes it. The ignored chunks are then handled by the retry mechanism.

Once the wallet sends all the chunks of the Vc to the verifier, it requests for a transfer report over `TransferReportRequestCharacteristic`. If the CRC checksum fails on the verifier, the complete VC transfer stops and the verifier throws an error and disconnects from the wallet.

The transfer report requested from the wallet is sent by verifier over `TransferReportResponseCharacteristic`.  If the CRC checksum fails on the wallet, the VC transfer is aborted and the verifier throws an error and disconnects from the verifier.

Once the complete VC is transferred, the VC getting saved or not by the verifier information is sent to wallet by  `VerificationStatusCharacteristic`. If the CRC checksum fails on the wallet, the wallet may not know about the status of the Vc being saved or not and  throws an error and disconnects from the verifier.


