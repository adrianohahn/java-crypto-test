package hahn.crypto.aes.decrypt;

import hahn.crypto.utilities.AESCryptoUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) {
        String keyPath = args[0];
        String encryptedFilePath = args[1];

        System.out.printf("AES encryptor parameters:%n" +
                        "\tKey file.......: %s%n" +
                        "\tEncrypted file.: %s%n",
                keyPath,
                encryptedFilePath);
        new Main().execute(keyPath, encryptedFilePath);
    }

    private void execute(String keyPath, String encryptedFilePath) {
        try {
            SecretKey secretKey = AESCryptoUtil.readKey(keyPath);
            byte[] encryptedData = readEncryptedFile(encryptedFilePath);
            String data = AESCryptoUtil.decrypt(encryptedData, secretKey);
            System.out.printf("The data is: %s%n", data);
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] readEncryptedFile(String encryptedFilePath) throws IOException {
        Path path = FileSystems.getDefault().getPath(encryptedFilePath);
        return Files.readAllBytes(path);

    }
}
