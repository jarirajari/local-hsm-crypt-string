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
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;
import java.util.Optional;

public class HSMCryptoTool {
    private static String ENV_CONFIG = "HSM";
    private static String DEFAULT_CONFIG = "hsm-pkcs11.conf";

    public void init() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Optional<String> config = Optional.ofNullable(System.getenv(ENV_CONFIG));
        System.out.println("file="+config.get());
        Provider hsmProvider = new sun.security.pkcs11.SunPKCS11(config.orElse(DEFAULT_CONFIG));
        System.out.println("hsmProvider="+hsmProvider.getInfo());
        KeyStore hsmKeyStore = KeyStore.getInstance("PKCS11", hsmProvider);
        String userPin = "1234"; // The pin to unlock the HSM
        hsmKeyStore.load(null, userPin.toCharArray());
        Enumeration<String> aliases = hsmKeyStore.aliases();
        for (Provider.Service service: hsmProvider.getServices()) {
            if (service.getType().startsWith("Cipher")) {
                System.out.println(service.getType() + " : " + service.getAlgorithm());
            }
        }

        while (aliases.hasMoreElements()) { // token has a single certificate
            String alias = aliases.nextElement();
            System.out.println(alias);
        }
        String keyID = "0x2323"; //"HSM RSA Key Java APP23"; //"HSM RSA Key Java APP23"; // The key identifier or alias
        String keyPin = "1234"; // Optional pin to unlock the key
        // PRI
        PrivateKey privateKey = (PrivateKey) hsmKeyStore.getKey(keyID, keyPin.toCharArray());
        // PUB
        X509Certificate cert = (X509Certificate) hsmKeyStore.getCertificate(keyID);
        PublicKey publicKey = cert.getPublicKey();

        String verySecretString = "http://jarirajari.wordpress.com";
        Cipher ec = null;
        // Choose one from the listed ciphers
        ec = Cipher.getInstance("RSA", hsmProvider);
        // cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        ec.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = ec.doFinal(verySecretString.getBytes(StandardCharsets.UTF_8));
        // reverse
        Cipher dc = null;
        // Choose one from the listed ciphers
        dc = Cipher.getInstance("RSA", hsmProvider);
        dc.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = dc.doFinal(encryptedBytes);
        System.out.println("ENcrypted string => "+new String(encryptedBytes));
        System.out.println("DEcrypted string => "+new String(decryptedBytes));
    }
}
