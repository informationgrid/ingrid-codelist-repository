package de.ingrid.codelistHandler.importer.inspireRegistry;

import de.ingrid.codelistHandler.importer.inspireRegistry.model.Item;
import de.ingrid.codelistHandler.importer.inspireRegistry.model.PriorityDatasetModel;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class InspireRegistryUtil {

    private static Logger log = Logger.getLogger(InspireRegistryUtil.class);

    public CodeList importFromRegistry(URL urlGerman, URL urlEnglish) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PriorityDatasetModel priorityDataset = objectMapper.readValue(urlGerman, PriorityDatasetModel.class);
        PriorityDatasetModel priorityDatasetEn = objectMapper.readValue(urlEnglish, PriorityDatasetModel.class);

        List<Item> items = priorityDataset.getItems();
        CodeList codelist = mapToCodelist(items);
        addEnglishVersion(codelist, priorityDatasetEn.getItems());

        return codelist;
    }

    private CodeList mapToCodelist(List<Item> items) {

        CodeList codeList = new CodeList();
        List<CodeListEntry> entries = new ArrayList<>();

        for (Item item : items) {
            CodeListEntry entry = new CodeListEntry();
            entry.setId(convertIdToNumber(item.value.id));
            entry.setField("de", item.value.label.text);
            entries.add(entry);
        }
        codeList.setEntries(entries);

        return codeList;
    }

    private String createDataField(Item item) {
        //language=JSON
        return "{\"url\":\"" + item.value.id + "\"," +
                " \"thesaurusTitle\": \"" + item.value.metadataCodelist.label.text + "\"," +
                " \"thesaurusId\": \"" + item.value.metadataCodelist.id + "\" " +
//                "  \"date\": \"" + item.value.date + "\"\n" +
                "}";
    }

    private void addEnglishVersion(CodeList codelist, List<Item> items) {
        for (Item item : items) {
            String id = convertIdToNumber(item.value.id);
            CodeListEntry entry = codelist.getEntries().stream().filter(c -> c.getId().equals(id)).findAny().orElseGet(null);
            if (entry == null) {
                log.error("No Codelist entry found for English version");
            } else {
                entry.setField("en", item.value.label.text);
                entry.setData(createDataField(item));
            }
        }
    }

    private String convertIdToNumber(String id) {
        return String.valueOf(id.hashCode());
    }
}
