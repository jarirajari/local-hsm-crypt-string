package org.example.main;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.concurrent.Callable;

@Command(name = "stringcrypt", mixinStandardHelpOptions = true, version = "stringcrypt 1.0",
        description = "Encrypt and decrypt strings using PKI!")
public class StringCrypt implements Callable<Integer> {

    @Parameters(index = "0", description = "Secret string")
    private String secret;

    @Option(names = {"-e", "--encrypt"}, description = "Encrypt mode")
    private Boolean encrypt = false;

    @Option(names = {"-d", "--decrypt"}, description = "Decrypt mode")
    private Boolean decrypt = false;

    @Override
    public Integer call() throws Exception {
        HSMCryptoTool tool = new HSMCryptoTool();
        byte[] secretString = this.secret.getBytes(StandardCharsets.UTF_8);
        byte[] output = new byte[0];
        try {
            if (encrypt == false && decrypt == false) {
                output = secretString;
            }
            if (encrypt) {
                output = tool.encryptString(secretString);
            }
            if (decrypt) {
                output = tool.decryptString(secretString);
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        // BE VERY CAREFUL WITH byte[] <=> String conversions!
        System.out.println(String.format("INPUT : %s", this.secret));
        System.out.println(String.format("OUTPUT: %s", new String (output)));

        return 0;
    }
}
