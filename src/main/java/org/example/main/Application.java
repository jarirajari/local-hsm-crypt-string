package org.example.main;

import picocli.CommandLine;
import java.security.Provider;
import java.security.Security;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;

public class Application {

    public static void main(String... args) {
        int exitCode = new CommandLine(new StringCrypt()).execute(args);
        System.exit(exitCode);
    }

    public static void initBC() {
        Provider bc = BouncyCastleProviderSingleton.getInstance();
        Security.addProvider(bc);
    }
}
