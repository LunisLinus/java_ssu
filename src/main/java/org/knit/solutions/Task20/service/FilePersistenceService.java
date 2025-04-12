package org.knit.solutions.Task20.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knit.solutions.Task20.crypto.EncryptionService;
import org.knit.solutions.Task20.model.PasswordEntry;
import org.knit.solutions.Task20.security.MasterPasswordHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilePersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(FilePersistenceService.class);
    private static final String FILE_PATH = "passwords.dat";

    private final EncryptionService encryptionService;
    private final MasterPasswordHolder masterPasswordHolder;
    private final ObjectMapper objectMapper;

    public FilePersistenceService(EncryptionService encryptionService, MasterPasswordHolder masterPasswordHolder) {
        this.encryptionService = encryptionService;
        this.masterPasswordHolder = masterPasswordHolder;
        this.objectMapper = new ObjectMapper();
    }

    public void save(List<PasswordEntry> entries) {
        try {
            String json = objectMapper.writeValueAsString(entries);
            char[] masterPassword = masterPasswordHolder.getMasterPassword();
            String encrypted = encryptionService.encrypt(json, masterPassword);
            Files.write(new File(FILE_PATH).toPath(), encrypted.getBytes(StandardCharsets.UTF_8));
            logger.info("Данные сохранены в файл {}", FILE_PATH);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении данных в файл", e);
        }
    }

    public List<PasswordEntry> load() {
        List<PasswordEntry> entries = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                logger.info("Файл {} не найден. Загружаем пустой список.", FILE_PATH);
                return entries;
            }
            byte[] bytes = Files.readAllBytes(file.toPath());
            String encrypted = new String(bytes, StandardCharsets.UTF_8);
            char[] masterPassword = masterPasswordHolder.getMasterPassword();
            String json = encryptionService.decrypt(encrypted, masterPassword);
            entries = objectMapper.readValue(json, new TypeReference<List<PasswordEntry>>() {});
            logger.info("Данные загружены из файла {}. Загружено {} записей.", FILE_PATH, entries.size());
        } catch (Exception e) {
            logger.error("Ошибка при загрузке данных из файла", e);
        }
        return entries;
    }
}

