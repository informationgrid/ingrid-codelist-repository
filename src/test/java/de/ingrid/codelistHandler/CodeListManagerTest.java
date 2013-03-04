package de.ingrid.codelistHandler;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
public class CodeListManagerTest {
    private String dataFile = "data/codelistsTests.xml";
    
    @Autowired
    private CodeListManager manager;

    private static int initialSize;
    
    @Before
    public void setUp() throws Exception {
        removeExisitingTestFile();
    }
    
    @Test
    public void testGetCodeLists_empty() {
        List<CodeList> cls = manager.getCodeLists();
        // initial list should be loaded from CodeListService!
        initialSize = cls.size();
        assertTrue(!cls.isEmpty());
    }
    
    @Test
    public void testGetCodeLists_filled() {
        fillData();
        List<CodeList> cls = manager.getCodeLists();
        assertEquals(cls.size(), initialSize+2);
    }

    
    
    private void removeExisitingTestFile() {
        File f = new File(dataFile);
        if (f.exists() && f.isFile()) {
            f.delete();
        }
    }
    
    private void fillData() {
        CodeList cl1 = new CodeList();
        cl1.setId("1");
        cl1.setName("INSPIRE-Topics");
        cl1.setDescription("Here are all the INSPIRE topics.");
        
        CodeListEntry entry1 = new CodeListEntry();
        entry1.setId("1");
        entry1.setLocalisedEntry("de", "Abfall");
        entry1.setLocalisedEntry("en", "Trash");
        
        CodeListEntry entry2 = new CodeListEntry();
        entry2.setId("2");
        entry2.setLocalisedEntry("de", "Geodaten");
        entry2.setLocalisedEntry("en", "Geodata");
        
        cl1.addEntry(entry1);
        cl1.addEntry(entry2);
        
        CodeList cl2 = new CodeList();
        cl2.setId("2");
        cl2.setName("Address-Type");
        cl2.setDescription("What kind of address can one have? Here you can find them all.");
        
        CodeListEntry entry_2_1 = new CodeListEntry();
        entry_2_1.setId("2");
        entry_2_1.setLocalisedEntry("de", "Telefon");
        entry_2_1.setLocalisedEntry("en", "Phone");
        
        cl2.addEntry(entry_2_1);
        
        List<CodeList> allCl = manager.getCodeLists();
        allCl.add(cl1);
        allCl.add(cl2);
        
        manager.writeCodeListsToFile();
    }

    public void setManager(CodeListManager manager) {
        this.manager = manager;
    }

    public CodeListManager getManager() {
        return manager;
    }
}
