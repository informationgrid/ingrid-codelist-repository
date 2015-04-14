/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2015 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.codelistHandler;

//import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context-test.xml"})
public class CodeListUpdateTest {
    private String dataFile = "data/codelistsTests.xml";
    private String dataFileAdd = "src/test/resources/updates/codelist_add.xml";
    private String dataFileRemove = "src/test/resources/updates/codelist_remove.xml";
    private String dataFileUpdate = "src/test/resources/updates/codelist_update.xml";
    private String dataFileAddEntry = "src/test/resources/updates/codelist_add_entry.xml";
    
    @Autowired
    private CodeListManager manager;

    @Before
    public void setUp() throws Exception {
        removeExisitingTestFile();
        manager.getCodeLists().clear();
    }
    
    @Test
    public void addCodelist() {
        // codelist "9876" is missing in the beginning
        assertThat( manager.getCodeList( "9876" ), is( nullValue() ) );
        
        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileAdd );
        
        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();
        
        // codelist "9876" should be available now
        assertThat( "Updated codelist should be available now!",  manager.getCodeList( "9876" ), is( not( nullValue() ) ) );
    }
    
    @Test
    public void removeCodelist() {
        // codelist "100" is available in the beginning
        assertThat( manager.getCodeList( "100" ), is( not( nullValue() ) ) );
        
        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileRemove );
        
        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();
        
        // codelist "100" should be removed now
        assertThat( "Updated codelist should be removed now!",  manager.getCodeList( "100" ), is( nullValue() ) );
    }
    
    @Test
    public void updateCodelist() {
        // codelist "100" is available in the beginning
        CodeList codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 35 ) );
        
        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileUpdate );
        
        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();
        
        // codelist "100" should still be there
        codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        List<CodeListEntry> entries = codeList100.getEntries();
        assertThat( entries.size(), is( 1 ) );
        assertThat( entries.get(0).getId(), is( "1234" ) );
        assertThat( entries.get(0).getData(), is( "no data" ) );
        assertThat( entries.get(0).getDescription(), is( "updated entry" ) );
        assertThat( entries.get(0).getLocalisations().get( "de" ), is( "München" ) );
        assertThat( entries.get(0).getLocalisations().get( "en" ), is( "Munich" ) );
        
    }
    
    @Test
    public void addCodelistEntry() {
        // codelist "100" has no entry 1234 in the beginning
        CodeList codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 35 ) );
        assertThat( manager.getCodeListEntry( "100", "1234" ), is( nullValue() ) );
        
        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileAddEntry );
        
        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();
        
        // codelist "100" should have entry 1234 now
        codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 36 ) );
        assertThat( manager.getCodeListEntry( "100", "1234" ), is( not( nullValue() ) ) );
    }
    
    @Test
    @Ignore
    public void removeCodelistEntry() {
        
    }
    
    @Test
    @Ignore
    public void updateCodelistEntry() {
        
    }
    
    
    
    private void removeExisitingTestFile() {
        File f = new File(dataFile);
        if (f.exists() && f.isFile()) {
            f.delete();
        }
    }

    public void setManager(CodeListManager manager) {
        this.manager = manager;
    }

    public CodeListManager getManager() {
        return manager;
    }
}
