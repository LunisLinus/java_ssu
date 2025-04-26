package org.knit.Task20;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knit.solutions.Task20.crypto.EncryptionService;
import org.knit.solutions.Task20.model.User;
import org.knit.solutions.Task20.security.MasterPasswordHolder;
import org.knit.solutions.Task20.service.FilePersistenceService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FilePersistenceServiceTest {

    private EncryptionService encryptionService;
    private MasterPasswordHolder masterPasswordHolder;
    private FilePersistenceService filePersistenceService;

    @BeforeEach
    void setUp() {
        encryptionService = mock(EncryptionService.class);
        masterPasswordHolder = mock(MasterPasswordHolder.class);
        filePersistenceService = new FilePersistenceService(encryptionService, masterPasswordHolder);
    }

    @Test
    void saveUser_shouldCreateFileWithEncryptedContent() throws Exception {
        User user = new User("testUser", System.currentTimeMillis(), Collections.emptyList());
        char[] masterPassword = "password".toCharArray();
        String encryptedData = "encryptedData";

        when(masterPasswordHolder.getMasterPassword()).thenReturn(masterPassword);
        when(encryptionService.encrypt(anyString(), eq(masterPassword))).thenReturn(encryptedData);

        filePersistenceService.saveUser(user);

        Path filePath = Path.of("users", "testUser.dat");
        assertThat(Files.exists(filePath)).isTrue();

        String fileContent = Files.readString(filePath);
        assertThat(fileContent).isEqualTo(encryptedData);

        Files.delete(filePath);
    }

    @Test
    void loadUser_shouldReturnNullIfFileNotExists() throws Exception {
        User result = filePersistenceService.loadUser("nonExistingUser");
        assertThat(result).isNull();
    }
}
