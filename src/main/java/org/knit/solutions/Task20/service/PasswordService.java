package org.knit.solutions.Task20.service;

import org.knit.solutions.Task20.clipboard.ClipboardService;
import org.knit.solutions.Task20.crypto.EncryptionService;
import org.knit.solutions.Task20.model.PasswordEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.knit.solutions.Task20.repository.PasswordRepository;
import org.knit.solutions.Task20.security.MasterPasswordHolder;

@Service
public class PasswordService {

    private final PasswordRepository repository;
    private final EncryptionService encryptionService;
    private final MasterPasswordHolder masterPasswordHolder;
    private final ClipboardService clipboardService;

    @Autowired
    public PasswordService(
            PasswordRepository repository,
            EncryptionService encryptionService,
            MasterPasswordHolder masterPasswordHolder,
            ClipboardService clipboardService
    ) {
        this.repository = repository;
        this.encryptionService = encryptionService;
        this.masterPasswordHolder = masterPasswordHolder;
        this.clipboardService = clipboardService;
    }

    public void addPassword(String site, String login, String rawPassword) {
        try {
            char[] masterPassword = masterPasswordHolder.getMasterPassword();
            String encrypted = encryptionService.encrypt(rawPassword, masterPassword);

            PasswordEntry entry = new PasswordEntry(site, login, encrypted);
            repository.addEntry(entry);
            System.out.println("Запись добавлена: " + site);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при шифровании пароля!");
        }
    }

    public void listAll() {
        System.out.println("Сохранённые сайты и логины:");
        repository.getAllEntries().forEach(entry ->
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
            System.out.println("Пароль для сайта " + site + " скопирован в буфер обмена.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при расшифровке пароля!");
        }
    }

    public void deletePassword(String site) {
        repository.deleteBySite(site);
        System.out.println("Запись для сайта " + site + " удалена.");
    }
}