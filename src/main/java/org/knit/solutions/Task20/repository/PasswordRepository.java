package org.knit.solutions.Task20.repository;

import org.knit.solutions.Task20.model.PasswordEntry;
import java.util.List;

public interface PasswordRepository {
    void addEntry(PasswordEntry entry);
    List<PasswordEntry> getAllEntries();
    PasswordEntry findBySite(String site);
    void deleteBySite(String site);
}
