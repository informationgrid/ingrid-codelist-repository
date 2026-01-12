/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2026 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.codelistHandler.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;

import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.util.CodeListUtils;

public class XmlCodeListUtilsTest {

    @Test
    final void testGetCodeListFromJsonGeneric() {
        String data = "{\"id\": \"100\",\"name\": \"TestList\",\"description\": \"Dies ist eine Testliste.\",\"entries\": [{\"id\": \"1\",\"localisations\": [[\"de\",\"eins\"],[\"en\",\"one\"]]}],\"lastModified\": 1329414705531}";
        CodeList cl = CodeListUtils.getCodeListFromJsonGeneric(data);
        assertNotNull(cl);
    }
}
