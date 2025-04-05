package org.knit.solutions.Task20.crypto;

public interface EncryptionService {
    String encrypt(String plainText, char[] masterPassword) throws Exception;
    String decrypt(String cipherText, char[] masterPassword) throws Exception;
}