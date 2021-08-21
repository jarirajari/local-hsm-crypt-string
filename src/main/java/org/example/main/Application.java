package org.example.main;

import picocli.CommandLine;

public class Application {

    public static void main(String... args) {
        int exitCode = new CommandLine(new Checksum()).execute(args);
        System.exit(exitCode);
    }
}
