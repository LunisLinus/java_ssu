package org.knit.solutions.Task20.service;

import org.knit.solutions.Task20.model.User;
import org.knit.solutions.Task20.security.MasterPasswordHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Console;
import java.util.Scanner;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String USERS_DIR = "users";

    private final FilePersistenceService filePersistenceService;
    private final MasterPasswordHolder masterPasswordHolder;
    private final PasswordService passwordService;  // если требуется использовать метод генерации мастер-пароля

    public UserService(FilePersistenceService filePersistenceService,
                       MasterPasswordHolder masterPasswordHolder,
                       PasswordService passwordService) {
        this.filePersistenceService = filePersistenceService;
        this.masterPasswordHolder = masterPasswordHolder;
        this.passwordService = passwordService;
    }

    public User getOrCreateUser(String username, Scanner scanner) {
        File userFile = new File(USERS_DIR, username + ".dat");
        User user;
        if (userFile.exists()) {
            logger.info("Найден файл пользователя: {}", userFile.getAbsolutePath());
            int attempts = 0;
            boolean loaded = false;
            user = null;
            while (!loaded && attempts < 3) {
                char[] masterPassword = getPassword(scanner, "Введите мастер-пароль для входа: ");
                masterPasswordHolder.setMasterPassword(masterPassword);
                user = filePersistenceService.loadUser(username);
                if (user != null) {
                    loaded = true;
                } else {
                    System.out.println("Неверный мастер-пароль. Попробуйте ещё раз.");
                    attempts++;
                }
            }
            if (!loaded) {
                System.out.println("Превышено число попыток ввода верного пароля. Завершение работы.");
                return null;
            }
        } else {
            System.out.println("Новый пользователь.");
            System.out.print("Желаете ли вы автоматически сгенерировать мастер-пароль? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) {
                String generatedMaster = passwordService.generatePassword(16);
                System.out.println("Сгенерированный мастер-пароль: " + generatedMaster);
                masterPasswordHolder.setMasterPassword(generatedMaster.toCharArray());
            } else {
                char[] masterPassword = getPassword(scanner, "Введите мастер-пароль: ");
                masterPasswordHolder.setMasterPassword(masterPassword);
            }
            user = new User(username, System.currentTimeMillis(), null);
            logger.info("Создаётся новый аккаунт для пользователя {}", username);
            System.out.println("Аккаунт создан.");
            filePersistenceService.saveUser(user);
        }
        return user;
    }

    private char[] getPassword(Scanner scanner, String prompt) {
        Console console = System.console();
        if (console != null) {
            return console.readPassword(prompt);
        } else {
            System.out.print(prompt);
            return scanner.nextLine().toCharArray();
        }
    }
}
