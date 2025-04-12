package org.knit.solutions.Task20.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.knit.solutions.Task20.crypto.EncryptionService;
import org.knit.solutions.Task20.model.User;
import org.knit.solutions.Task20.security.MasterPasswordHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class FilePersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(FilePersistenceService.class);
    private static final String USERS_DIR = "users";

    private final EncryptionService encryptionService;
    private final MasterPasswordHolder masterPasswordHolder;
    private final ObjectMapper objectMapper;

    public FilePersistenceService(EncryptionService encryptionService, MasterPasswordHolder masterPasswordHolder) {
        this.encryptionService = encryptionService;
        this.masterPasswordHolder = masterPasswordHolder;
        this.objectMapper = new ObjectMapper();
    }


    public void saveUser(User user) {
        try {
            String json = objectMapper.writeValueAsString(user);
            char[] masterPassword = masterPasswordHolder.getMasterPassword();
            String encrypted = encryptionService.encrypt(json, masterPassword);
            File dir = new File(USERS_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, user.getUsername() + ".dat");
            Files.write(file.toPath(), encrypted.getBytes(StandardCharsets.UTF_8));
            logger.info("Данные пользователя {} сохранены в файл {}", user.getUsername(), file.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Ошибка при сохранении данных пользователя", e);
        }
    }

    public User loadUser(String username) {
        User user = null;
        try {
            File file = new File(USERS_DIR, username + ".dat");
            if (!file.exists()) {
                logger.info("Файл для пользователя {} не найден.", username);
                return null;
            }
            byte[] bytes = Files.readAllBytes(file.toPath());
            String encrypted = new String(bytes, StandardCharsets.UTF_8);
            char[] masterPassword = masterPasswordHolder.getMasterPassword();
            String json = encryptionService.decrypt(encrypted, masterPassword);
            user = objectMapper.readValue(json, User.class);
            logger.info("Данные пользователя {} загружены. Загружено {} записей.", username, user.getEntries().size());
        } catch (Exception e) {
            logger.error("Ошибка при загрузке данных пользователя", e);
        }
        return user;
    }
}
