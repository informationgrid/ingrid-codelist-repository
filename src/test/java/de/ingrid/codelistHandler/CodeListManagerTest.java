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

    @Before
    public void setUp() throws Exception {
        removeExisitingTestFile();
    }
    
    @Test
    public void testGetCodeLists_empty() {
        List<CodeList> cls = manager.getCodeLists();
        // initial list should be loaded from CodeListService!
        assertTrue(!cls.isEmpty());
    }
    
    @Test
    public void testGetCodeLists_filled() {
    	List<CodeList> cls = manager.getCodeLists();
    	int size = cls.size();
        fillData();
        
        // clear list to read codelist from file again!
        cls.clear();
        
        cls = manager.getCodeLists();
        assertEquals(size+2, cls.size());
        
        CodeList cl = manager.getCodeList("1");
        assertEquals("INSPIRE-Topics", cl.getName());
        assertEquals("Abfall", cl.getEntries().get(0).getLocalisedEntry("de"));
        assertEquals("Trash", cl.getEntries().get(0).getLocalisedEntry("en"));
        assertEquals("meine daten", cl.getEntries().get(0).getData());
        assertEquals("1", cl.getEntries().get(0).getId());
        
        assertEquals("Geodaten", cl.getEntries().get(1).getLocalisedEntry("de"));
        assertEquals("Geodata", cl.getEntries().get(1).getLocalisedEntry("en"));
        assertEquals("meine anderen daten", cl.getEntries().get(1).getData());
        assertEquals("2", cl.getEntries().get(1).getId());
        
        
        cl = manager.getCodeList("2");
        assertEquals("Address-Type", cl.getName());
        assertEquals("Telefon", cl.getEntries().get(0).getLocalisedEntry("de"));
        assertEquals("Phone", cl.getEntries().get(0).getLocalisedEntry("en"));
        assertEquals("weitere daten", cl.getEntries().get(0).getData());
        assertEquals("2", cl.getEntries().get(0).getId());
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
        entry1.setData("meine daten");
        
        CodeListEntry entry2 = new CodeListEntry();
        entry2.setId("2");
        entry2.setLocalisedEntry("de", "Geodaten");
        entry2.setLocalisedEntry("en", "Geodata");
        entry2.setData("meine anderen daten");
        
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
        entry_2_1.setData("weitere daten");
        
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
