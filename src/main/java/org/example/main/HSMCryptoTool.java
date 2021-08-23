package org.example.main;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Optional;

public class HSMCryptoTool {
    private static String ENV_CONFIG = "HSM";
    private static String DEFAULT_CONFIG = "hsm-pkcs11.conf";
    private final String HSM_PIN = "1234";
    private Provider hsmProvider;
    private KeyStore hsmKeyStore;

    public HSMCryptoTool() {
        Optional<String> config = Optional.ofNullable(System.getenv(ENV_CONFIG));
        System.out.println("file="+config.get());

        this.hsmProvider = new sun.security.pkcs11.SunPKCS11(config.orElse(DEFAULT_CONFIG));
        System.out.println("hsmProvider="+this.hsmProvider.getInfo());

        try {
            this.hsmKeyStore = KeyStore.getInstance("PKCS11", this.hsmProvider);
            this.hsmKeyStore.load(null, HSM_PIN.toCharArray());
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException ex) {
            System.out.println("Could not initialize HSM: "+ex.getMessage());
        }
    }

    public byte[] /* encrypted */ encryptString(byte[] unencrypted) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher ec;
        byte[] encryptedBytes;

        ec = RSACipher(this.hsmProvider);
        ec.init(Cipher.ENCRYPT_MODE, publicKey().get());
        encryptedBytes = ec.doFinal(unencrypted);

        return encryptedBytes;
    }

    public byte[] /* unencrypted */ decryptString(byte[] encrypted) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher dc;
        byte[] decryptedBytes;

        dc = RSACipher(this.hsmProvider);
        dc.init(Cipher.DECRYPT_MODE, privateKey().get());
        decryptedBytes = dc.doFinal(encrypted);

        return decryptedBytes;
    }

    private static Cipher RSACipher(Provider p) {
        Cipher cipher;

        try {
            cipher = Cipher.getInstance("RSA", p); // PKCS11 has limitations => NoPadding has none!
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            cipher = null;
        }

        return cipher;
    }

    private Optional<PrivateKey> privateKey() {
        String keyID = "0x2323";
        String keyPIN = "1234";
        PrivateKey privateKey;

        try {
            privateKey = (PrivateKey) this.hsmKeyStore.getKey(keyID, keyPIN.toCharArray());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException ex) {
            privateKey = null;
        }

        return Optional.ofNullable(privateKey);
    }

    private Optional<PublicKey> publicKey() {
        String keyID = "0x2323";
        // no need for keyPIN for public key
        PublicKey publicKey;
        X509Certificate cert;

        try {
            cert = (X509Certificate) this.hsmKeyStore.getCertificate(keyID);
            publicKey = cert.getPublicKey();
        } catch (KeyStoreException e) {
            publicKey = null;
        }

        return Optional.ofNullable(publicKey);
    }
}
