package org.knit.solutions.Task20.repository;

import org.knit.solutions.Task20.model.PasswordEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryPasswordRepository implements PasswordRepository {

    private final Map<String, PasswordEntry> storage = new HashMap<>();

    @Override
    public void addEntry(PasswordEntry entry) {
        storage.put(entry.getSite(), entry);
    }

    @Override
    public List<PasswordEntry> getAllEntries() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public PasswordEntry findBySite(String site) {
        return storage.get(site);
    }

    @Override
    public void deleteBySite(String site) {
        storage.remove(site);
    }
}
