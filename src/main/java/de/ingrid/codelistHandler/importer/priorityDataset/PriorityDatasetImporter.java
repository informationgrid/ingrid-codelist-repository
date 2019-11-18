package de.ingrid.codelistHandler.importer.priorityDataset;

import de.ingrid.codelistHandler.importer.Importer;
import de.ingrid.codelistHandler.importer.priorityDataset.model.Item;
import de.ingrid.codelistHandler.importer.priorityDataset.model.PriorityDatasetModel;
import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriorityDatasetImporter implements Importer {

    private static Logger log = Logger.getLogger(PriorityDatasetImporter.class);

    public static final String CODELIST_ID = "6350";
    private static String DATA_URL_DE = "http://inspire.ec.europa.eu/metadata-codelist/PriorityDataset/PriorityDataset.de.json";
    private static String DATA_URL_EN = "http://inspire.ec.europa.eu/metadata-codelist/PriorityDataset/PriorityDataset.en.json";

    @Autowired
    CodeListService codeListService;

    @Override
    public void start() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = new URI(DATA_URL_DE).toURL();
            URL urlEn = new URI(DATA_URL_EN).toURL();
            PriorityDatasetModel priorityDataset = objectMapper.readValue(url, PriorityDatasetModel.class);
            PriorityDatasetModel priorityDatasetEn = objectMapper.readValue(urlEn, PriorityDatasetModel.class);

            List<Item> items = priorityDataset.getItems();
            CodeList codelist = mapToCodelist(items);
            addEnglishVersion(codelist, priorityDatasetEn.getItems());

            this.codeListService.setCodelist(CODELIST_ID, codelist);
            this.codeListService.persistToAll();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private CodeList mapToCodelist(List<Item> items) {

        CodeList codeList = new CodeList();
        codeList.setName("Priority Dataset");
        codeList.setId(CODELIST_ID);

        List<CodeListEntry> entries = new ArrayList<>();

        for (Item item : items) {
            CodeListEntry entry = new CodeListEntry();
            entry.setId(convertIdToNumber(item.value.id));
            entry.setField("de", item.value.label.text);
            entry.setData("{\"url\":\"" + item.value.id + "\"}");
            entries.add(entry);
        }
        codeList.setEntries(entries);

        return codeList;
    }


    private void addEnglishVersion(CodeList codelist, List<Item> items) {
        for (Item item : items) {
            String id = convertIdToNumber(item.value.id);
            CodeListEntry entry = codelist.getEntries().stream().filter(c -> c.getId().equals(id)).findAny().orElseGet(null);
            if (entry == null) {
                log.error("No Codelist entry found for English version");
            } else {
                entry.setField("en", item.value.label.text);
            }
        }
    }

    private String convertIdToNumber(String id) {
        return String.valueOf(id.hashCode());
    }
}
