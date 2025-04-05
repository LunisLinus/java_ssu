package org.knit.solutions.Task20.model;

public class PasswordEntry {
    private final String site;
    private final String login;
    private final String encryptedPassword;

    public PasswordEntry(String site, String login, String encryptedPassword) {
        this.site = site;
        this.login = login;
        this.encryptedPassword = encryptedPassword;
    }

    public String getSite() {
        return site;
    }

    public String getLogin() {
        return login;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}
