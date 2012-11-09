package de.ingrid.codelistHandler.update;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.XmlCodeListPersistency;

public class UpdateChecker {
    private final static Logger log = Logger.getLogger(UpdateChecker.class);
    
    @Autowired
    private CodeListService codelistService;

    public UpdateChecker() {}
    
    public CodeListService getCodelistService() {
        return codelistService;
    }

    public void setCodelistService(CodeListService codelistService) {
        this.codelistService = codelistService;
        
        this.execute();
    }

    private void execute() {
        // check for update file
        if (updateFilePresent()) {
            log.info("Update file found! Updating ...");
            // read update file
            List<CodeList> newCodelists = getNewCodelists();
            
            List<CodeList> currentCodelists = this.codelistService.getCodeLists();
            
            // make modifications
            for (CodeList codelist : newCodelists) {
                CodeList existingCodelist = this.codelistService.getCodeList(codelist.getId());
                if (existingCodelist == null) {
                    log.info("adding new codelist: " + codelist.getId());
                    existingCodelist = codelist;
                } else {
                    for (CodeListEntry entry : codelist.getEntries()) {
                        String existingEntry = this.codelistService.getCodeListValue(codelist.getId(), entry.getId(), "de");
                        if (existingEntry == null) {
                            log.info("adding new entry: " + entry.getId());
                            existingCodelist.addEntry(entry);
                        } else {
                            log.info("updating entry: " + entry.getId());
                            existingCodelist.removeEntry(entry.getId());
                            existingCodelist.addEntry(entry);
                            //throw new RuntimeException("Conflict updating Codelist-Entry: " + entry.getId() + " in Codelist: " + codelist.getId());
                        } 
                    }
                }
                
                this.codelistService.setCodelist(codelist.getId(), existingCodelist);
            }
            
            this.codelistService.persistToAll();
            
            // delete update file
            new File("update.xml").delete();
            
            log.info("Update finished!");
        }
        
    }

    private List<CodeList> getNewCodelists() {
        XmlCodeListPersistency persist = new XmlCodeListPersistency();
        persist.setPathToXml("update.xml");
        return persist.read();
    }

    private boolean updateFilePresent() {
        return new File("update.xml").exists();
    }
    
}
