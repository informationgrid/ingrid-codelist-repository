/*-
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2024 wemove digital solutions GmbH
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
package de.ingrid.codelistHandler;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import de.ingrid.codelistHandler.model.CodeListEntryUpdate;
import de.ingrid.codelistHandler.model.CodeListUpdate;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.XmlCodeListPersistency;

public class PatchCodelistSyntaxTest {

    @Test
    void test() {

        try {

            List<String> filesForUpdate = getPatchFiles();

            for (String file : filesForUpdate) {
                XmlCodeListPersistency<CodeListUpdate> xml = new XmlCodeListPersistency<>();
                xml.setPathToXml(file);
                List<CodeListUpdate> updateCodelists = xml.read();

                if (updateCodelists.isEmpty()) {
                    Assertions.fail("Error validating Codelist patch: " + file);
                }

                for (CodeListUpdate codeList : updateCodelists) {
                    switch (codeList.getType()) {
                        case ADD:
                        case UPDATE:
                            for (CodeListEntry cle : codeList.getCodelist().getEntries()) {
                                String data = cle.getData();
                                if (data != null && data.startsWith("{") && data.endsWith("}")) {
                                    try {
                                        new JSONObject( data );
                                    } catch (JSONException e) {
                                        Assertions.fail("Error in '" + file + "'JSON data field: " + data);
                                    }
                                }
                            }
                            break;
                        case ENTRYUPDATE:

                            String data;
                            for (CodeListEntryUpdate entry : codeList.getEntries()) {
                                switch (entry.getType()) {
                                    case ADD:
                                    case UPDATE:
                                        data = entry.getEntry().getData();
                                        if (data != null && data.startsWith("{") && data.endsWith("}")) {
                                            try {
                                                new JSONObject( data );
                                            } catch (JSONException e) {
                                                Assertions.fail("Error in '" + file + "'JSON data field: " + data);
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail("Error validating Codelist patches: " + e);
        }

    }

    private List<String> getPatchFiles() throws Exception {
        List<String> resList = new ArrayList<>();

        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourceResolver.getResources( "file:src/main/release/patches/*.xml" );
        for (Resource resource : resources) {
            if (resource.exists()) {
                resList.add( resource.getFile().getPath() );
            }
        }
        return resList;
    }

}
