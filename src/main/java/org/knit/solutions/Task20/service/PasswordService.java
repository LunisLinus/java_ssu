package org.knit.solutions.Task20.service;

import org.knit.solutions.Task20.clipboard.ClipboardService;
import org.knit.solutions.Task20.crypto.EncryptionService;
import org.knit.solutions.Task20.model.PasswordEntry;
import org.knit.solutions.Task20.model.User;
import org.knit.solutions.Task20.repository.PasswordRepository;
import org.knit.solutions.Task20.security.MasterPasswordHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);
    private static final long EIGHT_MONTHS_MILLIS = 240L * 86_400_000;

    private final PasswordRepository repository;
    private final EncryptionService encryptionService;
    private final MasterPasswordHolder masterPasswordHolder;
    private final ClipboardService clipboardService;
    private final FilePersistenceService filePersistenceService;

    private User currentUser;

    @Autowired
    public PasswordService(
            PasswordRepository repository,
            EncryptionService encryptionService,
            MasterPasswordHolder masterPasswordHolder,
            ClipboardService clipboardService,
            FilePersistenceService filePersistenceService
    ) {
        this.repository = repository;
        this.encryptionService = encryptionService;
        this.masterPasswordHolder = masterPasswordHolder;
        this.clipboardService = clipboardService;
        this.filePersistenceService = filePersistenceService;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void addPassword(String site, String login, String rawPassword) {
        try {
            char[] masterPassword = masterPasswordHolder.getMasterPassword();
            String encrypted = encryptionService.encrypt(rawPassword, masterPassword);
            PasswordEntry entry = new PasswordEntry(site, login, encrypted);
            repository.addEntry(entry);
            currentUser.getEntries().add(entry);
            logger.info("Добавлена запись: сайт {} с логином {}", site, login);
            filePersistenceService.saveUser(currentUser);
            System.out.println("Запись добавлена: " + site);
        } catch (Exception e) {
            logger.error("Ошибка при шифровании пароля для сайта " + site, e);
            System.out.println("Ошибка при шифровании пароля!");
        }
    }

    public void listAll() {
        System.out.println("Сохранённые сайты и логины:");
        currentUser.getEntries().forEach(entry ->
                System.out.println("Site: " + entry.getSite() + ", Login: " + entry.getLogin())
        );
    }

    public void copyPassword(String site) {
        PasswordEntry entry = repository.findBySite(site);
        if (entry == null) {
            System.out.println("Запись для сайта " + site + " не найдена.");
            return;
        }
        try {
            char[] masterPassword = masterPasswordHolder.getMasterPassword();
            String decrypted = encryptionService.decrypt(entry.getEncryptedPassword(), masterPassword);
            clipboardService.copyToClipboard(decrypted);
            logger.info("Скопирован пароль для сайта {}", site);
            System.out.println("Пароль для сайта " + site + " скопирован в буфер обмена.");

            CompletableFuture.delayedExecutor(30, TimeUnit.SECONDS).execute(() -> {
                clipboardService.copyToClipboard("");
                logger.info("Буфер обмена очищен автоматически после 30 секунд");
            });

        } catch (Exception e) {
            logger.error("Ошибка при расшифровке пароля для сайта " + site, e);
            System.out.println("Ошибка при расшифровке пароля!");
        }
    }

    public void deletePassword(String site) {
        repository.deleteBySite(site);
        currentUser.getEntries().removeIf(e -> e.getSite().equals(site));
        logger.info("Удалена запись для сайта {}", site);
        filePersistenceService.saveUser(currentUser);
        System.out.println("Запись для сайта " + site + " удалена.");
    }

    public String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    public void changeMasterPassword(String newMasterPassword) {
        masterPasswordHolder.setMasterPassword(newMasterPassword.toCharArray());
        currentUser.setLastPasswordChange(System.currentTimeMillis());
        filePersistenceService.saveUser(currentUser);
        logger.info("Мастер-пароль для пользователя {} обновлён", currentUser.getUsername());
        System.out.println("Мастер-пароль успешно обновлён.");
    }

    public boolean isMasterPasswordExpired() {
        long elapsed = System.currentTimeMillis() - currentUser.getLastPasswordChange();
        return elapsed > EIGHT_MONTHS_MILLIS;
    }
}
