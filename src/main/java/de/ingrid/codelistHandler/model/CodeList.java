package de.ingrid.codelistHandler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The model for a CodeList, which are going to be stored.
 * 
 * @author Andre
 *
 */
public class CodeList {
    private String  id;
    private String  name;
    private String  description;
    private List<CodeListEntry> entries;
    private long lastModified;
    
    public CodeList() {
        this.entries = new ArrayList<CodeListEntry>();
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setEntries(List<CodeListEntry> entries) {
        this.entries = entries;
    }
    public List<CodeListEntry> getEntries() {
        return entries;
    }
    
    public void addEntry(CodeListEntry entry) {
        this.entries.add(entry);
    }
    
    public void removeEntry(CodeListEntry entry) {
        this.entries.remove(entry);
    }
    
    public void removeEntry(String id) {
        for (CodeListEntry entry : entries) {
            if (entry.getId() == id) {
                entries.remove(entry);
                break;
            }
        }
    }

    public void setLastModified(long date) {
        this.lastModified = date;
    }

    public long getLastModified() {
        return lastModified;
    }

  
    
    
}
