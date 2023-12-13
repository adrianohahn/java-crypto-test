package hahn.crypto.aes.encrypt;

import hahn.crypto.utilities.AESCryptoUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Encrypt data using an AES key stored in a file.
 * Parameters:
 *      - Key file (input)
 *      - File with encrypted data (output)
 *      - Text to be encrypted
 */
public class Main {

    public static void main(String[] args) {
        String keyPath = args[0];
        String encryptedFilePath = args[1];
        String text = args[2];

        System.out.printf("AES encryptor parameters:%n" +
                "\tKey file.......: %s%n" +
                "\tEncrypted file.: %s%n" +
                "\tText...........: %s%n",
                keyPath,
                encryptedFilePath,
                text);
        new Main().execute(keyPath, encryptedFilePath, text);
    }

    private void execute(String keyPath, String encryptedFilePath, String text) {
        try {
            SecretKey secretKey = AESCryptoUtil.readKey(keyPath);
            byte[] encryptedData = AESCryptoUtil.encrypt(text, secretKey);
            writeDataToFile(encryptedFilePath, encryptedData);

        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeDataToFile(String encryptedFilePath, byte[] encryptedData) throws IOException {
        Path filePath = FileSystems.getDefault().getPath(encryptedFilePath);
        try (OutputStream out = Files.newOutputStream(filePath, CREATE_NEW)) {
            out.write(encryptedData);
        }
    }

}
