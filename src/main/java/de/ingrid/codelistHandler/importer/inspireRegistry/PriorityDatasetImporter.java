/*-
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
package de.ingrid.codelistHandler.importer.inspireRegistry;

import de.ingrid.codelistHandler.importer.Importer;
import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;

@Service
public class PriorityDatasetImporter implements Importer {

    private static Logger log = Logger.getLogger(PriorityDatasetImporter.class);

    public static final String CODELIST_ID = "6350";

    @Autowired
    CodeListService codeListService;

    @Autowired
    InspireRegistryUtil registry;

    @Value("${priority.dataset.de.resource.url}")
    private String dataUrlDE;

    @Value("${priority.dataset.en.resource.url}")
    private String dataUrlEN;

    @Override
    public void start() {
        CodeList oldCodelist = this.codeListService.getCodeList(CODELIST_ID);

        try {
            URL url = new URI(dataUrlDE).toURL();
            URL urlEn = new URI(dataUrlEN).toURL();

            log.info("Import PriorityDataset code list " + CODELIST_ID + " from INSPIRE registry.");
            log.info("Source PriorityDataset (de): " + dataUrlDE);
            log.info("Source PriorityDataset (en): " + dataUrlEN);

            CodeList codelist = registry.importFromRegistry(url, urlEn);
            log.info("Import successful.");
            codelist.setName("Priority Dataset");
            codelist.setId(CODELIST_ID);

            boolean hasChanged = !codelist.equals(oldCodelist);

            if (hasChanged) {
                log.info("Priority Dataset has changed and is updated");
                this.codeListService.setCodelist(CODELIST_ID, codelist);
                this.codeListService.persistToAll();
            } else {
                log.debug("Priority Dataset has not been changed and is not updated");
            }

        } catch (Exception e) {
            log.error("Problem synchronizing priority dataset from INSPIRE registry", e);
        }

    }

}
