package org.example.main;

import picocli.CommandLine;
import java.security.Provider;
import java.security.Security;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;

public class Application {

    /*
     * All of the tokens and their objects are stored in the location given by softhsm2.conf. 
     * Backup can thus be done as a regular file copy.
     */
    public static void main(String... args) {
        HSMCryptoTool tool = new HSMCryptoTool();
        try {
            tool.init();
        } catch (Exception e) {
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
