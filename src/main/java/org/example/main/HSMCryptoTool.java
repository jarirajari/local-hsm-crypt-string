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
        try {
            Optional<String> config = Optional.ofNullable(System.getenv(ENV_CONFIG));
            if (! config.isPresent()) {
                throw new RuntimeException("No PKCS11 config file present: EXPORT HSM=</absolute/filen.name>");
            }
            this.hsmProvider = new sun.security.pkcs11.SunPKCS11(config.orElse(DEFAULT_CONFIG));
            this.hsmKeyStore = KeyStore.getInstance("PKCS11", this.hsmProvider);
            this.hsmKeyStore.load(null, HSM_PIN.toCharArray());
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException ex) {
            System.err.println("Could not initialize HSM: "+ex.getMessage());
        } finally {
            System.out.println("Using crypto provider="+this.hsmProvider.getInfo());
        }
    }

    public byte[] /* encrypted */ encryptString(byte[] unencrypted) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher ec;
        byte[] encryptedBytes;

        ec = RSACipher(this.hsmProvider);
        ec.init(Cipher.ENCRYPT_MODE, publicKey().get());
        encryptedBytes = ec.doFinal(unencrypted);

        return Base64.getEncoder().encode(encryptedBytes);
    }

    public byte[] /* unencrypted */ decryptString(byte[] encrypted) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher dc;
        byte[] decryptedBytes;
        byte[] b64 = Base64.getDecoder().decode(encrypted);

        dc = RSACipher(this.hsmProvider);
        dc.init(Cipher.DECRYPT_MODE, privateKey().get());
        decryptedBytes = dc.doFinal(b64);

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
