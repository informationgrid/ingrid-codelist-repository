package de.ingrid.codelistHandler.importer.priorityDataset;

import de.ingrid.codelistHandler.importer.Importer;
import de.ingrid.codelistHandler.importer.priorityDataset.model.Item;
import de.ingrid.codelistHandler.importer.priorityDataset.model.PriorityDatasetModel;
import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
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

    public static final String CODELIST_ID = "6350";
    private static String DATA_URL = "http://inspire.ec.europa.eu/metadata-codelist/PriorityDataset/PriorityDataset.de.json";

    @Autowired
    CodeListService codeListService;

    @Override
    public void start() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = new URI(DATA_URL).toURL();
            PriorityDatasetModel priorityDataset = objectMapper.readValue(url, PriorityDatasetModel.class);

            List<Item> items = priorityDataset.getItems();
            CodeList codelist = mapToCodelist(items);

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
            entry.setField(item.value.label.lang, item.value.label.text);
            // since no german translation yet, use the English version
            entry.setField("de", item.value.label.text);
            entry.setData("{\"url\":\"" + item.value.id + "\"}");
            entries.add(entry);
        }
        codeList.setEntries(entries);

        return codeList;
    }

    private String convertIdToNumber(String id) {
        return String.valueOf(id.hashCode());
    }
}
