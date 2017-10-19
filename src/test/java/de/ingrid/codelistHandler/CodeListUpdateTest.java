/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2017 wemove digital solutions GmbH
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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.ingrid.codelistHandler.migrate.Migrator;
import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.ICodeListPersistency;
import de.ingrid.codelists.persistency.XmlCodeListPersistency;
import de.ingrid.codelists.util.VersionUtils;

public class CodeListUpdateTest {
    private static String dataFile = "data/codelistsTests";
    private String dataFileAdd = "src/test/resources/updates/V1_codelist_add.xml";
    private String dataFileRemove = "src/test/resources/updates/V2_codelist_remove.xml";
    private String dataFileUpdate = "src/test/resources/updates/V3_codelist_update.xml";
    private String dataFileAddEntry = "src/test/resources/updates/V3a_codelist_add_entry.xml";
    private String dataFileRemoveEntry = "src/test/resources/updates/V4_codelist_remove_entry.xml";
    private String dataFileUpdateEntry = "src/test/resources/updates/V4a_codelist_update_entry.xml";
    private String dataFileMultiple = "src/test/resources/updates/V4b_codelist_multiple_changes.xml";

    private static CodeListManager manager;
    
    private static int counter = 0;

    @Before
    public void setUp() throws Exception {
        
        CodeListService cls = new CodeListService();
        List<ICodeListPersistency> persistencies = new ArrayList<ICodeListPersistency>();
        XmlCodeListPersistency<CodeList> xmlCodeListPersistency = new XmlCodeListPersistency<CodeList>();
        xmlCodeListPersistency.setPathToXml( "data/codelistsTests" + (counter++) );
        persistencies.add( xmlCodeListPersistency );
        cls.setPersistencies( persistencies );
        cls.setDefaultPersistency( 0 );
        removeExisitingTestFile();
        
        manager = new CodeListManager( cls, new Migrator() );
        manager.getCodeLists().clear();
        // copyUpdateFiles();
    }

    private void copyUpdateFiles() {
            try {
                Files.list(Paths.get("src/test/resources/updates"))
                .forEach( file -> {
                    try {
                        Files.copy( file, Paths.get( "target/test-classes/updates", file.getFileName().toString() ), StandardCopyOption.REPLACE_EXISTING );
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }        
                });
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    @After
    public void reset() throws Exception {
        // reset private field
        Field field = CodeListManager.class.getDeclaredField( "PATH_CODELIST_UPDATES" );
        field.setAccessible( true );
        field.set( manager, "classpath:patches/*.xml" );
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
        CodeList codelist = manager.getCodeList( "9876" );
        assertThat( "Updated codelist should be available now!", codelist, is( not( nullValue() ) ) );
        assertThat( "Updated codelist should be available now!", codelist.getLastModified(), greaterThan( 10000l ) );
    }

    @Test
    public void removeCodelist() {
        // codelist "101" is available in the beginning
        assertThat( manager.getCodeList( "101" ), is( not( nullValue() ) ) );

        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileRemove );

        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();

        // codelist "101" should be removed now
        assertThat( "Updated codelist should be removed now!", manager.getCodeList( "101" ), is( nullValue() ) );
    }

    @Test
    public void updateCodelist() {
        // codelist "100" is available in the beginning
        CodeList codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 48 ) );

        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileUpdate );

        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();

        // codelist "100" should still be there
        codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        List<CodeListEntry> entries = codeList100.getEntries();
        assertThat( entries.size(), is( 1 ) );
        assertThat( entries.get( 0 ).getId(), is( "1234" ) );
        assertThat( entries.get( 0 ).getData(), is( "no data" ) );
        assertThat( entries.get( 0 ).getDescription(), is( "updated entry" ) );
        assertThat( entries.get( 0 ).getLocalisations().get( "de" ), is( "München" ) );
        assertThat( entries.get( 0 ).getLocalisations().get( "en" ), is( "Munich" ) );

    }

    @Test
    public void addCodelistEntry() {
        // codelist "100" has no entry "1234" in the beginning
        CodeList codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 48 ) );
        assertThat( manager.getCodeListEntry( "100", "1234" ), is( nullValue() ) );

        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileAddEntry );

        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();

        // codelist "100" should have entry 1234 now
        codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 49 ) );
        assertThat( manager.getCodeListEntry( "100", "1234" ), is( not( nullValue() ) ) );
    }

    @Test
    public void removeCodelistEntry() {
        // codelist "100" has an entry "3068" in the beginning
        CodeList codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 48 ) );
        assertThat( manager.getCodeListEntry( "100", "4178" ), is( not( nullValue() ) ) );

        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileRemoveEntry );

        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();

        // codelist "100" should have no entry "3068" now
        codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 47 ) );
        assertThat( manager.getCodeListEntry( "100", "4178" ), is( nullValue() ) );
    }

    @Test
    public void updateCodelistEntry() {
        // codelist "100" has an entry "3068" in the beginning
        CodeList codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 48 ) );
        assertThat( manager.getCodeListEntry( "100", "3068" ), is( not( nullValue() ) ) );

        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileUpdateEntry );

        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();

        // codelist "100" should have entry 1234 now
        codeList100 = manager.getCodeList( "100" );
        assertThat( codeList100, is( not( nullValue() ) ) );
        assertThat( codeList100.getEntries().size(), is( 48 ) );
        CodeListEntry entry = manager.getCodeListEntry( "100", "3068" );
        assertThat( entry, is( not( nullValue() ) ) );
        assertThat( entry.getId(), is( "3068" ) );
        assertThat( entry.getData(), is( "data for 3068" ) );
        assertThat( entry.getDescription(), is( "description for 3068" ) );
        assertThat( entry.getLocalisedEntry( "de" ), is( "Eintrag Nummer 3068" ) );
        assertThat( entry.getLocalisedEntry( "en" ), is( "Entry number 3068" ) );
    }

    @Test
    @Ignore
    public void multipleChanges() {

        assertThat( manager.getCodeList( "98765" ), is( nullValue() ) );
        assertThat( manager.getCodeList( "87654" ), is( nullValue() ) );

        // codelist "100" has an entry "3068" in the beginning
        // CodeList codeList100 = manager.getCodeList( "100" );
        // assertThat( codeList100, is( not( nullValue() ) ) );
        // assertThat( codeList100.getEntries().size(), is( 35 ) );
        // assertThat( manager.getCodeListEntry( "100", "3068" ), is( not(
        // nullValue() ) ) );

        // read update codelist file and apply
        manager.updateCodelistsFromUpdateFile( dataFileMultiple );

        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();

        assertThat( manager.getCodeList( "98765" ), is( not( nullValue() ) ) );
        assertThat( manager.getCodeList( "87654" ), is( not( nullValue() ) ) );

        // codelist "100" should have entry 1234 now
        // codeList100 = manager.getCodeList( "100" );
        // assertThat( codeList100, is( not( nullValue() ) ) );
        // assertThat( codeList100.getEntries().size(), is( 35 ) );
        // CodeListEntry entry = manager.getCodeListEntry( "100", "3068" );
        // assertThat( entry, is( not( nullValue() ) ) );
        // assertThat( entry.getId(), is( "3068" ) );
        // assertThat( entry.getData(), is( "data for 3068" ) );
        // assertThat( entry.getDescription(), is( "description for 3068" ) );
        // assertThat( entry.getLocalisedEntry( "de" ), is( "Eintrag Nummer
        // 3068" ) );
        // assertThat( entry.getLocalisedEntry( "en" ), is( "Entry number 3068"
        // ) );
    }

    @Test
    public void getCurrentVersion() throws Exception {
        File file = new File( "data/version.info" );
        file.delete();
        assertThat( VersionUtils.getCurrentVersion(), is( "0" ) );
        writeToFile( "1" );
        assertThat( VersionUtils.getCurrentVersion(), is( "1" ) );
        writeToFile( "1b" );
        assertThat( VersionUtils.getCurrentVersion(), is( "1b" ) );
    }

    @Test
    public void checkLatestVersion() throws Exception {
        // change private field
        Field field = CodeListManager.class.getDeclaredField( "PATH_CODELIST_UPDATES" );
        field.setAccessible( true );
        field.set( manager, "classpath:updates/*.xml" );
        
        copyUpdateFiles();

        List<String> files = manager.checkFilesForUpdate( "0" );
        assertThat( files.size(), is( 7 ) );

        files = manager.checkFilesForUpdate( "V1" );
        assertThat( files.size(), is( 6 ) );

        files = manager.checkFilesForUpdate( "V4" );
        assertThat( files.size(), is( 2 ) );

        files = manager.checkFilesForUpdate( "V4b" );
        assertThat( files.size(), is( 0 ) );

        files = manager.checkFilesForUpdate( "V6" );
        assertThat( files.size(), is( 0 ) );
    }

    @Test
    public void updateCodelistsAutomatically() throws Exception {
        // change private field
        Field field = CodeListManager.class.getDeclaredField( "PATH_CODELIST_UPDATES" );
        field.setAccessible( true );
        field.set( manager, "classpath:updates/*.xml" );
        
        removeExisitingTestFile();
        
        copyUpdateFiles();
        
        assertThat( VersionUtils.getCurrentVersion(), is( "0" ) );
        
        // TODO: check for preconditions
        assertThat( manager.getCodeList( "9876" ), is( nullValue() ) );
        assertThat( manager.getCodeListEntry( "100", "4178" ), is( not( nullValue() ) ) );
        
        // do the update
        manager.checkForUpdates();
        
        // let's load the file again to see if everything was saved correctly
        manager.getCodeLists().clear();

        // check for changes in codelists
        assertThat( manager.getCodeList( "9876" ), not( is( nullValue() ) ) );
        assertThat( manager.getCodeListEntry( "100", "4178" ), is( nullValue() ) );
        
        assertThat( VersionUtils.getCurrentVersion(), is( "V4b" ) );
    }

    private void writeToFile(String text) throws FileNotFoundException {
        PrintWriter out = new PrintWriter( "data/version.info" );
        out.print( text );
        out.close();
    }

    private static void removeExisitingTestFile() {
        Path rootPath = Paths.get("data");
        if (!rootPath.toFile().exists()) return;
        
        try {
            Files.walk(rootPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setManager(CodeListManager manager) {
        CodeListUpdateTest.manager = manager;
    }

    public CodeListManager getManager() {
        return manager;
    }
}
