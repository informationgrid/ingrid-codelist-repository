/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2023 wemove digital solutions GmbH
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

import de.ingrid.codelistHandler.CodeListManager;
import de.ingrid.codelistHandler.migrate.Migrator;
import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.comm.HttpCLCommunication;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.persistency.XmlCodeListPersistency;
import de.ingrid.codelists.util.CodeListUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

public class CodeListAccessResourceInitialTest {

    private static final String dataFile = "data/codelistsTests";

    private final CodeListManager manager;

    public CodeListAccessResourceInitialTest() {
        CodeListService codeListService = new CodeListService();
        codeListService.setComm(new HttpCLCommunication());
        XmlCodeListPersistency xmlCodeListPersistency = new XmlCodeListPersistency();
        xmlCodeListPersistency.setPathToXml("src/test/resources/codelists");
        codeListService.setPersistencies(List.of(xmlCodeListPersistency));
        manager = new CodeListManager(codeListService, new Migrator());
    }

    @BeforeAll
    public static void cleanUp() {
    }

    @BeforeEach
    public void setUp() {
        removeExisitingTestFile();
        manager.getCodeLists().clear();
    }

    @Test
    final void testGetCodeListsSortIdDec() {
        List<CodeList> list = manager.getCodeListsAsJson("id", null, CodeListUtils.SORT_DECREMENT);
        assertTrue(list.size() > 0);
        assertEquals(list.get(0).getId(), "99999999");
        assertEquals(list.get(list.size() - 1).getId(), "100");

        long previousId = 99999999L;
        for (CodeList o : list) {
            long nextId = Long.parseLong(o.getId());
            assertTrue(previousId >= nextId);
            previousId = nextId;
        }
    }

    @Test
    final void testGetCodeListsSortIdInc() {
        List<CodeList> list = manager.getCodeListsAsJson("id", null, CodeListUtils.SORT_INCREMENT);
        assertTrue(list.size() > 0);
        assertEquals(list.get(0).getId(), "100");
        assertEquals(list.get(list.size() - 1).getId(), "99999999");

        long previousId = 100L;
        for (CodeList codeList : list) {
            long nextId = Long.parseLong(codeList.getId());
            assertTrue(previousId <= nextId);
            previousId = nextId;
        }
    }

    @Test
    final void testGetCodeListsSortNameDec() {
        List<CodeList> list = manager.getCodeListsAsJson("name", null, CodeListUtils.SORT_DECREMENT);
        assertTrue(list.size() > 0);
        assertEquals(list.get(0).getName(), "Zeitbezug des Datensatzes - Typ");

        String previousName = "Zeitbezug des Datensatzes - Typ";
        for (CodeList codeList : list) {
            if (codeList.getName() != null) {
                if (previousName == null) {
                    fail("Wrong String sorting! NULL value inbetween valid values!");
                }
                assertTrue(previousName.compareTo(codeList.getName()) >= 0);
                previousName = codeList.getName();
            } else {
                previousName = null;
            }
        }
    }

    @Test
    final void testGetCodeListsSortNameInc() {
        List<CodeList> list = manager.getCodeListsAsJson("name", null, CodeListUtils.SORT_INCREMENT);
        assertTrue(list.size() > 0);
        assertEquals(notNullValue(), list.get(0).getName());
        assertEquals(list.get(list.size() - 1).getName(), "Zeitbezug des Datensatzes - Typ");


        String previousName = null;
        for (CodeList codeList : list) {
            if (codeList.getName() != null) {
                if (previousName == null) {
                    previousName = codeList.getName();
                } else {
                    assertTrue(previousName.compareTo(codeList.getName()) <= 0);
                    previousName = codeList.getName();
                }
            } else {
                if (previousName != null) {
                    fail("Wrong String sorting! NULL value inbetween valid values!");
                }
                previousName = null;
            }
        }
    }

    @Test
    final void testGetCodeListsLastModifiedNow() {
        List<CodeList> list = manager.getCodeListsAsJson("name", String.valueOf(System.currentTimeMillis()), CodeListUtils.SORT_INCREMENT);
        assertEquals(list.size(), 0);
    }

    @Test
    final void testGetCodeListsLastModifiedEarlier() {
        List<CodeList> list = manager.getCodeListsAsJson("name", "1331741000000", CodeListUtils.SORT_INCREMENT);
        assertThat(list.size(), is(1));
        assertEquals("1000", list.get(0).getId());
    }

    private static void removeExisitingTestFile() {
        File f = new File(dataFile);
        if (f.exists() && f.isFile()) {
            f.delete();
        }
        f = new File("data/version.info");
        if (f.exists() && f.isFile()) {
            f.delete();
        }
    }
}
