package org.knit.Task20;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knit.solutions.Task20.clipboard.ClipboardService;
import org.knit.solutions.Task20.crypto.EncryptionService;
import org.knit.solutions.Task20.model.PasswordEntry;
import org.knit.solutions.Task20.model.User;
import org.knit.solutions.Task20.repository.PasswordRepository;
import org.knit.solutions.Task20.security.MasterPasswordHolder;
import org.knit.solutions.Task20.service.FilePersistenceService;
import org.knit.solutions.Task20.service.PasswordService;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PasswordServiceTest {

    private PasswordRepository repository;
    private EncryptionService encryptionService;
    private MasterPasswordHolder masterPasswordHolder;
    private FilePersistenceService filePersistenceService;
    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        repository = mock(PasswordRepository.class);
        encryptionService = mock(EncryptionService.class);
        masterPasswordHolder = mock(MasterPasswordHolder.class);
        ClipboardService clipboardService = mock(ClipboardService.class);
        filePersistenceService = mock(FilePersistenceService.class);

        passwordService = new PasswordService(repository, encryptionService, masterPasswordHolder, clipboardService, filePersistenceService);
    }

    @Test
    void addPassword_shouldEncryptAndSaveUser() throws Exception {
        User user = new User("testUser", System.currentTimeMillis(), new ArrayList<>());
        char[] masterPassword = "password".toCharArray();
        String encryptedPassword = "encryptedPassword";

        when(masterPasswordHolder.getMasterPassword()).thenReturn(masterPassword);
        when(encryptionService.encrypt("plainPassword", masterPassword)).thenReturn(encryptedPassword);

        passwordService.setCurrentUser(user);
        passwordService.addPassword("site.com", "login", "plainPassword");

        verify(repository).addEntry(any(PasswordEntry.class));
        verify(filePersistenceService).saveUser(user);
        assertThat(user.getEntries()).hasSize(1);
    }

    @Test
    void generatePassword_shouldReturnCorrectLength() {
        String generated = passwordService.generatePassword(16);
        assertThat(generated).hasSize(16);
    }

    @Test
    void deletePassword_shouldRemoveEntryAndSaveUser() {
        User user = new User("testUser", System.currentTimeMillis(), new ArrayList<>());
        PasswordEntry entry = new PasswordEntry("site.com", "login", "encryptedPassword");
        user.getEntries().add(entry);

        passwordService.setCurrentUser(user);

        passwordService.deletePassword("site.com");

        assertThat(user.getEntries()).isEmpty();
        verify(repository).deleteBySite("site.com");
        verify(filePersistenceService).saveUser(user);
    }
}
