package hahn.crypto.aes.cli;

import hahn.crypto.utilities.AESCryptoUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.nio.file.StandardOpenOption.CREATE;

public class Main {

    public static final String KEY_PATH = "aes-cli.key";

    public static void main(String[] args) throws Exception {
        new Main().execute(args);
    }

    private void execute(String[] args) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("help")) {
                printHelp(options);
                System.exit(0);
            }

            if (line.hasOption("generateKey")) {
                generateKey();
            }

            SecretKey key = readSecretKey();
            String password = readPassword();
            byte[] encryptedPassword = AESCryptoUtil.encrypt(password, key);

            Path filePath = FileSystems.getDefault().getPath("aes-cli.data");
            try (OutputStream out = Files.newOutputStream(filePath, CREATE)) {
                out.write(encryptedPassword);
            }


        } catch (ParseException e) {
            System.err.printf("%s%n", e.getMessage());
            printHelp(options);
        }
    }

    private void generateKey() throws NoSuchAlgorithmException, IOException {
        SecretKey key = AESCryptoUtil.generateRandomKey();
        AESCryptoUtil.storeKey(key, KEY_PATH);
    }

    private String readPassword() {
        String password;
        Console console = System.console();

        if (console == null) {
            System.out.println("Error: console is not available");
            System.exit(8);
        }

        while (true) {
            password = Arrays.toString(console.readPassword("Enter the password: "));
            var passwordConfirmation = Arrays.toString(console.readPassword("Confirm the password: "));

            if (password.equals(passwordConfirmation)) {
                break; // exit loop
            }

            System.out.println("The passwords do not match.");

        }

        return password;

    }

    private SecretKey readSecretKey() throws IOException, NoSuchAlgorithmException {
        return AESCryptoUtil.readKey(KEY_PATH);
    }

    private Options createOptions() {
        Options options = new Options();

        options.addOption("help", "print help message");
        options.addOption(Option.builder("generateKey")
                .argName("generateKey")
                .desc("Generate a new key file. Should be informed the first time the utility is executed")
                .hasArg(false)
                .build()
        );

        return options;
    }

    private void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("hahn.crypto.aes.cli.Main", options);
    }

}
