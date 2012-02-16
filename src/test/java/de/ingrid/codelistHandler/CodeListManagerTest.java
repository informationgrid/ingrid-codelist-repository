package de.ingrid.codelistHandler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.Writer;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import de.ingrid.codelistHandler.CodeListManager;
import de.ingrid.codelistHandler.model.CodeList;
import de.ingrid.codelistHandler.model.CodeListEntry;

public class CodeListManagerTest {

    @Before
    public void setUp() throws Exception {
        removeExisitingTestFile();
        fillData();
        
    }

    @Test
    public final void testGetCodeLists() {
        CodeListManager.getInstance().readCodeListsFromFile();
        fail("Not yet implemented"); // TODO
    }
    
    private void removeExisitingTestFile() {
        File f = new File("data/CodeListsData.xml");
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
        
        List<CodeList> allCl = CodeListManager.getInstance().getCodeLists();
        allCl.add(cl1);
        allCl.add(cl2);
        
        CodeListManager.getInstance().writeCodeListsToFile();
    }

}
