package hahn.crypto.utilities;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Utility class with Common methods for AES algorithm.
 */
public class AESCryptoUtil {

    public static final String ALGORITHM = "AES";

    /**
     * Generate an AES key using SecureRandom.
     * @return AES Key
     * @throws NoSuchAlgorithmException AES algorithm is not available
     */
    public static SecretKey generateRandomKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        keyGenerator.init(256, random);
        return keyGenerator.generateKey();
    }


    /**
     * Store a key in a new file.
     * @param secretKey Key to be stored.
     * @param keyPath file path. The file must not exist.
     * @throws IOException Invalid keyPath or error creating file
     */
    public static void storeKey(SecretKey secretKey, String keyPath) throws IOException {
        Path storeKeyPath = FileSystems.getDefault().getPath(keyPath);
        try (OutputStream out = Files.newOutputStream(storeKeyPath, CREATE_NEW)) {
            out.write(secretKey.getEncoded());
        }
    }

    /**
     * Read a key from a file
     * @param keyPath file path containing the key.
     * @return SecretKey from the given file.
     * @throws IOException Invalid keyPath or error reading file
     * @throws NoSuchAlgorithmException AES algorithm is not available
     */
    public static SecretKey readKey(String keyPath) throws IOException, NoSuchAlgorithmException {
        Path path = FileSystems.getDefault().getPath(keyPath);
        byte[] keyBytes = Files.readAllBytes(path);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Encrypt a string using a given key.
     * @param text string to be encrypted
     * @param secretKey encryption key
     * @return encrypted bytes
     * @throws NoSuchPaddingException When padding mechanism is not available
     * @throws NoSuchAlgorithmException AES algorithm is not available
     * @throws InvalidKeyException Invalid AES key informed
     * @throws IllegalBlockSizeException Usually not thrown for encryption
     * @throws BadPaddingException Usually not thrown for encryption
     */
    public static byte[] encrypt(String text, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(text.getBytes());
    }

    /**
     * Decrypt data using a given key.
     * @param encryptedData encrypted data
     * @param secretKey encryption key
     * @return Decrypted data
     * @throws NoSuchPaddingException When padding mechanism is not available
     * @throws NoSuchAlgorithmException AES algorithm is not available
     * @throws InvalidKeyException Invalid AES key informed
     * @throws IllegalBlockSizeException When the encrypted data has the wrong block size
     * @throws BadPaddingException When the encrypted data has the wrong padding
     */
    public static String decrypt(byte[] encryptedData, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(encryptedData));
    }
}
