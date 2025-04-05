package org.knit.solutions.Task20.crypto;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AesEncryptionService implements EncryptionService {

    private static final byte[] SALT = "FixedSaltValue".getBytes();
    private static final int ITERATIONS = 65536;
    private static final int KEY_SIZE = 256;

    @Override
    public String encrypt(String plainText, char[] masterPassword) throws Exception {
        SecretKeySpec keySpec = createKeySpec(masterPassword);

        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

        byte[] ivAndCiphertext = new byte[ivBytes.length + encrypted.length];
        System.arraycopy(ivBytes, 0, ivAndCiphertext, 0, ivBytes.length);
        System.arraycopy(encrypted, 0, ivAndCiphertext, ivBytes.length, encrypted.length);

        return Base64.getEncoder().encodeToString(ivAndCiphertext);
    }

    @Override
    public String decrypt(String cipherText, char[] masterPassword) throws Exception {
        SecretKeySpec keySpec = createKeySpec(masterPassword);

        byte[] ivAndCipherText = Base64.getDecoder().decode(cipherText);

        byte[] ivBytes = new byte[16];
        System.arraycopy(ivAndCipherText, 0, ivBytes, 0, ivBytes.length);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        byte[] encryptedBytes = new byte[ivAndCipherText.length - 16];
        System.arraycopy(ivAndCipherText, 16, encryptedBytes, 0, encryptedBytes.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted, "UTF-8");
    }

    private SecretKeySpec createKeySpec(char[] masterPassword) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(masterPassword, SALT, ITERATIONS, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
}
