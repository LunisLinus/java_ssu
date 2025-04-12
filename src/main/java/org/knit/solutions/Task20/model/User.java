package org.knit.solutions.Task20.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String username;
    private long lastPasswordChange;
    private final List<PasswordEntry> entries;

    @JsonCreator
    public User(@JsonProperty("username") String username,
                @JsonProperty("lastPasswordChange") long lastPasswordChange,
                @JsonProperty("entries") List<PasswordEntry> entries) {
        this.username = username;
        this.lastPasswordChange = lastPasswordChange;
        this.entries = (entries != null) ? entries : new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public long getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(long lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public List<PasswordEntry> getEntries() {
        return entries;
    }
}
