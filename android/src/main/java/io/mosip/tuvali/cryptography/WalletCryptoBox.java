package io.mosip.tuvali.cryptography;

public interface WalletCryptoBox {
    byte[] publicKey();
    int testVar;
    SecretsTranslator buildSecretsTranslator(byte[] walletPublicKey);
}
