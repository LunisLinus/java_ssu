package org.knit.solutions;

import org.knit.TaskDescription;
import org.knit.solutions.Task20.config.AppConfig;
import org.knit.solutions.Task20.model.PasswordEntry;
import org.knit.solutions.Task20.model.User;
import org.knit.solutions.Task20.repository.PasswordRepository;
import org.knit.solutions.Task20.service.FilePersistenceService;
import org.knit.solutions.Task20.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.knit.solutions.Task20.security.MasterPasswordHolder;
import org.knit.solutions.Task20.service.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/*
использование Spring Context,
AES-шифрование паролей с мастер-паролем (char[]),
команда для копирования пароля в буфер обмена (без вывода в консоль),
возможное расширение на JavaFX UI.
🧪 Лабораторная работа: Password Manager с Spring и шифрованием
🎯 Цель работы:
Познакомиться с использованием Spring Framework для создания приложений со слоистой архитектурой, управлением зависимостями, конфигурацией контекста и безопасной работой с данными.

📌 Задание:
Реализуйте приложение Password Manager, которое:

позволяет сохранять логины и пароли для различных сайтов,
использует Spring Context для управления зависимостями,
шифрует пароли с помощью мастер-пароля,
не показывает пароль на экран при просмотре, а копирует его в буфер обмена по команде,
Реализовать сохранение данных в файл (JSON, XML или Serialized) между сессиями.
(дополнительно) предоставляет графический интерфейс с JavaFX.
⚙️ Технические требования
✅ Основной функционал:
Приложение работает через консоль.
При запуске пользователь вводит мастер-пароль (не отображается на экране).
Все пароли шифруются с использованием алгоритма AES и введённого мастер-пароля.
Реализованы команды:
add — добавить запись (сайт, логин, пароль),
list — отобразить список сайтов и логинов (без паролей),
copy <site> — расшифровать пароль и скопировать в буфер обмена,
delete <site> — удалить запись по названию сайта,
exit — завершить программу.
🧱 Архитектура:
model/PasswordEntry.java — класс с полями: site, login, encryptedPassword.
repository/PasswordRepository — интерфейс + InMemoryPasswordRepository (использует HashMap).
service/PasswordService — бизнес-логика: добавление, удаление, копирование.
crypto/EncryptionService — интерфейс + AesEncryptionService.
security/MasterPasswordHolder — хранит мастер-пароль в char[].
clipboard/ClipboardService — интерфейс + SystemClipboardService (реализация копирования в буфер).
config/AppConfig.java — конфигурация Spring Context.
App.java — точка входа, CLI-обработчик.
🔒 Безопасность:
Мастер-пароль хранится в char[] и может быть обнулён вручную (в shutdown hook).
Пароли не отображаются на экране при вводе и расшифровке.
Шифрование происходит через AES/CBC/PKCS5Padding, ключ генерируется из мастер-пароля через PBKDF2WithHmacSHA256.
🧠 Дополнительно (по желанию):
Добавить графический интерфейс JavaFX, в котором:
пользователь может добавить и удалить записи;
таблица отображает логины и сайты;
кнопка "Показать пароль" копирует его в буфер (не отображает);
мастер-пароль запрашивается при запуске.
🔧 Подсказки
📥 Чтение мастер-пароля:
System.out.print("Введите мастер-пароль: ");
char[] masterPassword = System.console() != null
        ? System.console().readPassword()
        : scanner.nextLine().toCharArray();
📋 Копирование в буфер:
Toolkit.getDefaultToolkit()
       .getSystemClipboard()
       .setContents(new StringSelection(password), null);
🧼 Очистка мастер-пароля в конце:
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    MasterPasswordHolder holder = context.getBean(MasterPasswordHolder.class);
    holder.clear(); // обнулить char[] в памяти
}));
📁 Пример структуры проекта:
PasswordManager/
├── App.java
├── config/
│   └── AppConfig.java
├── model/
│   └── PasswordEntry.java
├── service/
│   └── PasswordService.java
├── crypto/
│   ├── EncryptionService.java
│   └── AesEncryptionService.java
├── security/
│   └── MasterPasswordHolder.java
├── repository/
│   ├── PasswordRepository.java
│   └── InMemoryPasswordRepository.java
├── clipboard/
│   ├── ClipboardService.java
│   └── SystemClipboardService.java
🧠 Что оценивается:
корреткное разделение логики на слои;
грамотное использование Spring для внедрения зависимостей;
безопасная работа с данными (char[], шифрование);
умение работать с консольным вводом, буфером обмена;
(дополнительно) использование JavaFX или сериализации.

🧩 Дополнительные задания (максимум +8 баллов)
🔍 1. Логгирование действий пользователя (+2 балла)
Использовать SLF4J + Logback (или аналогичный логгер).
Логировать команды пользователя (add, copy, delete, exit) и исключения.
Уровни логов:
INFO — действия пользователя,
ERROR — ошибки и сбои.
💾 2. Сохранение и загрузка данных в файл (+3 балла)
При запуске загружать данные из файла, при изменениях — сохранять автоматически.
Форматы: JSON, XML или Java Serialization.
⚠️ Файл должен быть полностью зашифрован с использованием мастер-пароля.
Например, можно:
сериализовать/сохранить в JSON;
зашифровать итоговую строку или байты через AES и сохранить;
при загрузке — расшифровать и восстановить объект.
Ключ должен быть получен из мастер-пароля (PBKDF2).
⏱️ 3. Асинхронная очистка буфера обмена (+3 балла)
После команды copy пароль попадает в буфер обмена.
Через 30 секунд (или другой указанный интервал) буфер должен быть автоматически очищен.
Очистка должна быть реализована через ScheduledExecutorService или CompletableFuture.delayedExecutor.
 */

@TaskDescription(taskNumber = 20, taskDescription = "Password Manager с Spring и шифрованием")
public class Task20Solution implements Solution {
    private static final Logger logger = LoggerFactory.getLogger(Task20Solution.class);

    @Override
    public void execute() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        MasterPasswordHolder holder = context.getBean(MasterPasswordHolder.class);
        PasswordService passwordService = context.getBean(PasswordService.class);
        FilePersistenceService filePersistenceService = context.getBean(FilePersistenceService.class);
        UserService userService = context.getBean(UserService.class);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            holder.clear();
            logger.info("Мастер-пароль очищен из памяти (shutdown hook).");
            System.out.println("Мастер-пароль очищен из памяти (shutdown hook).");
        }));

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine().trim();

        User user = userService.getOrCreateUser(username, scanner);
        if (user == null) {
            context.close();
            return;
        }
        passwordService.setCurrentUser(user);

        if (passwordService.isMasterPasswordExpired()) {
            System.out.println("Внимание! Мастер-пароль необходимо сменить (старше 8 месяцев).");
        }

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;
            String[] parts = line.split("\\s+");
            String command = parts[0].toLowerCase();
            logger.info("Пользовательская команда: {}", line);

            switch (command) {
                case "add":
                    if (parts.length < 4) {
                        System.out.println("Использование: add <site> <login> <password>");
                    } else {
                        String site = parts[1];
                        String login = parts[2];
                        String rawPassword = parts[3];
                        passwordService.addPassword(site, login, rawPassword);
                    }
                    break;
                case "list":
                    passwordService.listAll();
                    break;
                case "copy":
                    if (parts.length < 2) {
                        System.out.println("Использование: copy <site>");
                    } else {
                        passwordService.copyPassword(parts[1]);
                    }
                    break;
                case "delete":
                    if (parts.length < 2) {
                        System.out.println("Использование: delete <site>");
                    } else {
                        passwordService.deletePassword(parts[1]);
                    }
                    break;
                case "generate":
                    int length = 12;
                    if (parts.length >= 2) {
                        try {
                            length = Integer.parseInt(parts[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("Неверная длина, используется значение по умолчанию 12");
                        }
                    }
                    String generated = passwordService.generatePassword(length);
                    System.out.println("Сгенерированный пароль: " + generated);
                    break;
                case "change":
                    char[] newMaster = getPassword(scanner, "Введите новый мастер-пароль: ");
                    passwordService.changeMasterPassword(new String(newMaster));
                    break;
                case "exit":
                    logger.info("Пользователь завершает работу (exit).");
                    System.out.println("Завершение работы...");
                    filePersistenceService.saveUser(passwordService.getCurrentUser());
                    context.close();
                    return;
                default:
                    System.out.println("Неизвестная команда. Доступные команды: add, list, copy, delete, generate, change, exit.");
                    logger.warn("Неизвестная команда: {}", command);
            }
        }
    }

    private static char[] getPassword(Scanner scanner, String prompt) {
        Console console = System.console();
        if (console != null) {
            return console.readPassword(prompt);
        } else {
            System.out.print(prompt);
            return scanner.nextLine().toCharArray();
        }
    }
}