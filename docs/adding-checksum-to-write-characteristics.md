# Adding Checksum to all Write characteristics

CRC value is appended at the end of the data to be written in each characteristic and consumes 2 bytes. CRC checksum values have been added to the following characteristics :

- IdentifyRequestCharacteristic
- ResponseSizeCharacteristic
- SubmitResponseCharacteristic
- TransferReportRequestCharacteristic
- TransferReportResponseCharacteristic
- VerificationStatusCharacteristic
- DisconnectCharacteristic

When the wallet writes public key to the `IdentifyRequestCharacteristic` and the CRC checksum verification of the data fails on the verifier side, the verifier throws an error and disconnects from the wallet.

Before starting the VC transfer, the size of the VC is sent to the verifier on `ResponseSizeCharacteristic`. If the CRC checksum verification of the data fails on the verifier, it  throws an error and disconnects from the wallet.

Once the VC sharing starts, each chunks of the VC is sent on `SubmitResponseCharacteristic`. If the checksum verification of the data fails for any chunk, the verifier logs the error and ignores the chunk. It waits for the next chunk to receive and processes it. The ignored chunks are then handled by the retry mechanism.

Once the wallet sends all the chunks of the Vc to the verifier, it requests for a transfer report over `TransferReportRequestCharacteristic`. If the CRC checksum verification of the data fails on the verifier, the complete VC transfer stops and the verifier throws an error and disconnects from the wallet.

The transfer report requested from the wallet is sent by verifier over `TransferReportResponseCharacteristic`.  If the CRC checksum verification of the data fails on the wallet, the VC transfer is aborted and the verifier throws an error and disconnects from the verifier.

Once the complete VC is transferred, the status of the VC getting saved or not by the verifier is sent to wallet by  `VerificationStatusCharacteristic`. If the CRC checksum verification of the data fails on the wallet, the wallet may not know about the status of the VC being saved or not and  throws an error and disconnects from the verifier.

### Exception and Error Codes:

- VerifierDataReceivedCrcFailedException: This exception is thrown when the wallet receives any data from the verifier and the CRC checksum verification of the data fails.  Failed to transfer message will be displayed on the wallet application with the error code `TVW_TRA_003`

- WalletDataReceivedCrcFailedException: This exception is thrown when the verifier receives any data from the wallet and the CRC checksum verification of the data fails. Failed to transfer message will be displayed on the wallet application with the error code `TVV_TRA_003`

- DataCorruptionException:  This exception is thrown when the size of the public key of the wallet received by the verifier is smaller than the expected size. Failed to transfer message will be displayed on the wallet application with the error code `TVV_TRA_004`
