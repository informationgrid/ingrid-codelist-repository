package de.ingrid.codelistHandler.importer.inspireRegistry;

import de.ingrid.codelistHandler.importer.Importer;
import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class PriorityDatasetImporter implements Importer {

    private static Logger log = Logger.getLogger(PriorityDatasetImporter.class);

    public static final String CODELIST_ID = "6350";
    private static String DATA_URL_DE = "http://inspire.ec.europa.eu/metadata-codelist/PriorityDataset/PriorityDataset.de.json";
    private static String DATA_URL_EN = "http://inspire.ec.europa.eu/metadata-codelist/PriorityDataset/PriorityDataset.en.json";

    @Autowired
    CodeListService codeListService;

    @Autowired
    InspireRegistryUtil registry;

    @Override
    public void start() {

        try {
            URL url = new URI(DATA_URL_DE).toURL();
            URL urlEn = new URI(DATA_URL_EN).toURL();

            CodeList codelist = registry.importFromRegistry(url, urlEn);
            codelist.setName("Priority Dataset");
            codelist.setId(CODELIST_ID);

            this.codeListService.setCodelist(CODELIST_ID, codelist);
            this.codeListService.persistToAll();

        } catch (URISyntaxException | IOException e) {
            log.error("Problem synchronizing priority dataset from INSPIRE registry", e);
        }

    }

}
