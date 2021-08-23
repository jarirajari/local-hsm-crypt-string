package org.example.main;

import picocli.CommandLine;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Provider;
import java.security.Security;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Application {

    /*
     * All of the tokens and their objects are stored in the location given by softhsm2.conf. 
     * Backup can thus be done as a regular file copy.
     */
    public static void main(String... args) {
        HSMCryptoTool tool = new HSMCryptoTool();
        String verySecretString = "https://jarirajari.wordpress.com";
        try {
            // BE VERY CAREFUL WITH byte[] <=> String conversions!
            byte[] es = tool.encryptString(verySecretString.getBytes(StandardCharsets.UTF_8));
            System.out.println(es);
            byte[] ds = tool.decryptString(es);
            System.out.println(ds);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        int exitCode = new CommandLine(new Checksum()).execute(args);
        System.exit(exitCode);
    }

    public static void initBC() {
        Provider bc = BouncyCastleProviderSingleton.getInstance();
        Security.addProvider(bc);
    }
}
