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
    // the id of the codelist
    private String  id;
    
    // the name of the codelist
    private String  name;
    
    // a description of the codelist
    private String  description;
    
    // the entry that should be selected as default
    private String  defaultEntry;
    
    // several entries of a codelist, which can be localised
    private List<CodeListEntry> entries;
    
    // the date the codelist has been modified the last time
    private long lastModified;
    
    public CodeList() {
        this.entries = new ArrayList<CodeListEntry>();
        defaultEntry = "";
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
    
    public void setDefaultEntry(String defaultEntry) {
        this.defaultEntry = defaultEntry;
    }

    public String getDefaultEntry() {
        return defaultEntry;
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
