package io.mosip.tuvali.cryptography;

public interface WalletCryptoBox {
    byte[] publicKey();
    int test;
    SecretsTranslator buildSecretsTranslator(byte[] walletPublicKey);
}
