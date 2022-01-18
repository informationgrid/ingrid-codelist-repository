/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2022 wemove digital solutions GmbH
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
package de.ingrid.codelistHandler.rest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.ingrid.codelistHandler.CodeListManager;
import de.ingrid.codelists.util.CodeListUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context-rest-test.xml"})
public class CodeListAccessResourceInitialTest {

    private static String dataFile = "data/codelistsTests";

    @Autowired
    private CodeListManager manager;
    
    @BeforeClass
    public static void cleanUp() throws Exception {
//        CodeListService cls = new CodeListService();
//        List<ICodeListPersistency> persistencies = new ArrayList<ICodeListPersistency>();
//        XmlCodeListPersistency<CodeList> xmlCodeListPersistency = new XmlCodeListPersistency<CodeList>();
//        xmlCodeListPersistency.setPathToXml( "data/codelistsTests.xml" );
//        persistencies.add( xmlCodeListPersistency );
//        cls.setPersistencies( persistencies );
//        cls.setDefaultPersistency( 0 );
//        manager = new CodeListManager( cls );
    }
    
    @Before
    public void setUp() throws Exception {
        // change private field
//        Field field = CodeListManager.class.getDeclaredField( "PATH_CODELIST_UPDATES" );
//        field.setAccessible( true );
//        field.set( manager, "xxx" );
        
        removeExisitingTestFile();
        manager.getCodeLists().clear();
    }
    
    @Test
    public final void testGetCodeListsSortIdDec() throws JSONException {
        JSONArray list = new JSONArray((String)manager.getCodeListsAsJson("id", null, CodeListUtils.SORT_DECREMENT));
        assertTrue(list.length() > 0);
        assertTrue(((JSONObject)list.get(0)).get("id").equals("99999999"));
        assertTrue(((JSONObject)list.get(list.length()-1)).get("id").equals("100"));
        
        Long previousId = 99999999L;
        for (int i=0; i<list.length(); i++) {
            JSONObject o = (JSONObject) list.get(i);
            assertTrue(previousId >= o.getLong("id"));
            previousId = o.getLong("id");
        }
    }
    
    @Test
    public final void testGetCodeListsSortIdInc() throws JSONException {
        JSONArray list = new JSONArray((String)manager.getCodeListsAsJson("id", null, CodeListUtils.SORT_INCREMENT));
        assertTrue(list.length() > 0);
        assertTrue(((JSONObject)list.get(0)).get("id").equals("100"));
        assertTrue(((JSONObject)list.get(list.length()-1)).get("id").equals("99999999"));
        
        Long previousId = 100L;
        for (int i=0; i<list.length(); i++) {
            JSONObject o = (JSONObject) list.get(i);
            assertTrue(previousId <= o.getLong("id"));
            previousId = o.getLong("id");
        }
    }
    
    @Test
    public final void testGetCodeListsSortNameDec() throws JSONException {
        JSONArray list = new JSONArray((String)manager.getCodeListsAsJson("name", null, CodeListUtils.SORT_DECREMENT));
        assertTrue(list.length() > 0);
        assertTrue(((JSONObject)list.get(0)).get("name").equals("Zeitbezug des Datensatzes - Typ"));
        assertTrue(((JSONObject)list.get(list.length()-1)).has("name") == false);
        
        String previousName = "Zeitbezug des Datensatzes - Typ";
        for (int i=0; i<list.length(); i++) {
            JSONObject o = (JSONObject) list.get(i);
            if (o.has("name")) {
                if (previousName == null) {
                    assertTrue("Wrong String sorting! NULL value inbetween valid values!", false);
                }
                assertTrue(previousName.compareTo(o.getString("name")) >= 0);
                previousName = o.getString("name");
            } else {
                previousName = null;
            }
        }
    }
    
    @Test
    public final void testGetCodeListsSortNameInc() throws JSONException {
        JSONArray list = new JSONArray((String)manager.getCodeListsAsJson("name", null, CodeListUtils.SORT_INCREMENT));
        assertTrue(list.length() > 0);
        assertTrue(((JSONObject)list.get(0)).has("name") == false);
        assertTrue(((JSONObject)list.get(list.length()-1)).get("name").equals("Zeitbezug des Datensatzes - Typ"));
        
        
        String previousName = null;
        for (int i=0; i<list.length(); i++) {
            JSONObject o = (JSONObject) list.get(i);
            if (o.has("name")) {
                if (previousName == null) {
                    previousName = o.getString("name");
                } else {
                    assertTrue(previousName.compareTo(o.getString("name")) <= 0);
                    previousName = o.getString("name");
                }
            } else {
                if (previousName != null) {
                  assertTrue("Wrong String sorting! NULL value inbetween valid values!", false);
                }
                previousName = null;
            }
        }
    }
 
    @Test
    public final void testGetCodeListsLastModifiedNow() throws JSONException {
        JSONArray list = new JSONArray((String)manager.getCodeListsAsJson("name", String.valueOf(System.currentTimeMillis()), CodeListUtils.SORT_INCREMENT));
        assertTrue(list.length() == 0);
    }
    
    @Test
    public final void testGetCodeListsLastModifiedEarlier() throws JSONException {
        JSONArray list = new JSONArray((String)manager.getCodeListsAsJson("name", "1331741000000", CodeListUtils.SORT_INCREMENT));
        assertThat( list.length(), is( 1 ) );
        JSONObject o = (JSONObject) list.get(0);
        assertTrue("1000".equals(o.getString("id")));
    }
    
    public void setManager(CodeListManager manager) {
        this.manager = manager;
    }
    
    private static void removeExisitingTestFile() {
        File f = new File( dataFile );
        if (f.exists() && f.isFile()) {
            f.delete();
        }
        f = new File( "data/version.info" );
        if (f.exists() && f.isFile()) {
            f.delete();
        }
    }
}
