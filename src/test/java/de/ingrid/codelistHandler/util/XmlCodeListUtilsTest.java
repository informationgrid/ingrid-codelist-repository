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
package de.ingrid.codelistHandler.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.util.CodeListUtils;

public class XmlCodeListUtilsTest {

    @Before
    public void setUp() throws Exception {
        
        //XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
        //xstream.registerConverter(new JavaBeanConverter(xstream.getMapper()));
        
//        xstream.alias("codelist", CodeList.class);
//        xstream.alias("entry", CodeListEntry.class);
        
        /*
        CodeList c1 = new CodeList();
        c1.setId("testId");
        CodeListEntry ce1 = new CodeListEntry();
        ce1.setId("testCLE1");
        ce1.setLocalisedEntry("de", "Test");
        List<CodeListEntry> arr = new ArrayList<CodeListEntry>();
        arr.add(ce1);
        c1.setEntries(arr);
        
        xstream.toXML(c1);*/
        
        //CodeList cl = (CodeList)xstream.fromXML(data);
    }

    @Test
    public final void testGetCodeListFromJsonGeneric() {
        String data = "{\"id\": \"100\",\"name\": \"TestList\",\"description\": \"Dies ist eine Testliste.\",\"entries\": [{\"id\": \"1\",\"localisations\": [[\"de\",\"eins\"],[\"en\",\"one\"]]}],\"lastModified\": 1329414705531}";
        //String data = "{codelist:{\"id\": \"100\",\"name\": \"TestList\",\"description\": \"Dies ist eine Testliste.\",\"entries\": [],\"lastModified\": 1329414705531}}";
        CodeList cl = CodeListUtils.getCodeListFromJsonGeneric(data);
        assertNotNull(cl);
    }
}
