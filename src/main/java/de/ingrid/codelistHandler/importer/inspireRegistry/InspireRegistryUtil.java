/*-
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2025 wemove digital solutions GmbH
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
package de.ingrid.codelistHandler.importer.inspireRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ingrid.codelistHandler.importer.inspireRegistry.model.Item;
import de.ingrid.codelistHandler.importer.inspireRegistry.model.InspireCodelistModel;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.model.CodeListEntryStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class InspireRegistryUtil {

    private static final Logger log = Logger.getLogger(InspireRegistryUtil.class);

    public CodeList importFromRegistry(URL urlGerman, URL urlEnglish) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InspireCodelistModel inspireCodelist;
        InspireCodelistModel inspireCodelistEn;
        try {
            inspireCodelist = objectMapper.readValue(urlGerman, InspireCodelistModel.class);
        } catch (Exception e) {
            log.error("Error parsing JSON data from " + urlGerman, e);
            throw e;
        }
        try {
            inspireCodelistEn = objectMapper.readValue(urlEnglish, InspireCodelistModel.class);
        } catch (Exception e) {
            log.error("Error parsing JSON data from " + urlEnglish, e);
            throw e;
        }

        List<Item> items = inspireCodelist.getItems();
        CodeList codelist = mapToCodelist(items);
        addEnglishVersion(codelist, inspireCodelistEn.getItems());

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

    private CodeListEntryStatus getStatusFromField(String statusId) {

        return switch (statusId) {
            case "http://inspire.ec.europa.eu/registry/status/submitted" -> CodeListEntryStatus.SUBMITTED;
            case "http://inspire.ec.europa.eu/registry/status/superseded" -> CodeListEntryStatus.SUPERCEDED;
            case "http://inspire.ec.europa.eu/registry/status/valid" -> CodeListEntryStatus.VALID;
            case "http://inspire.ec.europa.eu/registry/status/invalid" -> CodeListEntryStatus.INVALID;
            case "http://inspire.ec.europa.eu/registry/status/retired" -> CodeListEntryStatus.RETIRED;
            default -> {
                log.warn("Unknown status-id: " + statusId);
                yield null;
            }
        };
    }

    private String createDataField(Item item) {
        //language=JSON
        return "{\"url\":\"" + item.value.id + "\"," +
                " \"thesaurusTitle\": \"" + item.value.metadataCodelist.label + "\"," +
                " \"thesaurusId\": \"" + item.value.metadataCodelist.id + "\"," +
//                "  \"date\": \"" + item.value.date + "\"\n" +
                "  \"status\": \"" + getStatusFromField(item.value.status.id) + "\"" +
                "}";
    }
    private void addEnglishVersion(CodeList codelist, List<Item> items) {
        for (Item item : items) {
            String id = convertIdToNumber(item.value.id);
            CodeListEntry entry = codelist.getEntries().stream().filter(c -> c.getId().equals(id)).findAny().orElse(null);
            if (entry == null) {
                log.error("No Codelist entry found for English version of: " + item.value.id + "(" + id + ")");
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
