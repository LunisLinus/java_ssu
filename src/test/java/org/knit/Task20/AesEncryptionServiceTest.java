package org.knit.Task20;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knit.solutions.Task20.crypto.AesEncryptionService;

import static org.assertj.core.api.Assertions.assertThat;

class AesEncryptionServiceTest {

    private AesEncryptionService aesEncryptionService;

    @BeforeEach
    void setUp() {
        aesEncryptionService = new AesEncryptionService();
    }

    @Test
    void encryptAndDecrypt_shouldReturnOriginalText() throws Exception {
        String plainText = "mySecretPassword";
        char[] masterPassword = "strongMasterPassword".toCharArray();

        String encrypted = aesEncryptionService.encrypt(plainText, masterPassword);
        String decrypted = aesEncryptionService.decrypt(encrypted, masterPassword);

        assertThat(decrypted).isEqualTo(plainText);
    }

    @Test
    void encrypt_shouldProduceDifferentResultsForDifferentPasswords() throws Exception {
        String plainText = "mySecretPassword";
        char[] masterPassword1 = "passwordOne".toCharArray();
        char[] masterPassword2 = "passwordTwo".toCharArray();

        String encrypted1 = aesEncryptionService.encrypt(plainText, masterPassword1);
        String encrypted2 = aesEncryptionService.encrypt(plainText, masterPassword2);

        assertThat(encrypted1).isNotEqualTo(encrypted2);
    }

    @Test
    void encrypt_shouldProduceDifferentResultsForSamePasswordDueToRandomIV() throws Exception {
        String plainText = "mySecretPassword";
        char[] masterPassword = "samePassword".toCharArray();

        String encrypted1 = aesEncryptionService.encrypt(plainText, masterPassword);
        String encrypted2 = aesEncryptionService.encrypt(plainText, masterPassword);

        assertThat(encrypted1).isNotEqualTo(encrypted2);
    }
}
