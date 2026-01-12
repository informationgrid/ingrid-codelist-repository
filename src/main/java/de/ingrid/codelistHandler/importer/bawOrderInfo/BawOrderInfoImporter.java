/*-
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
package de.ingrid.codelistHandler.importer.bawOrderInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ingrid.codelistHandler.importer.Importer;
import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class BawOrderInfoImporter implements Importer {

    private static Logger log = Logger.getLogger(BawOrderInfoImporter.class);

    public static final String CODELIST_ID = "bawOrderInfo";

    @Autowired
    CodeListService codeListService;


    @Value("${baw.orderinfo.rest.url}")
    private String orderInfoUrl;


    @Override
    public void start() {

        try {
            if(orderInfoUrl == null || orderInfoUrl.isEmpty()) {
                log.debug("No BAW OrderInfo URL configured, skipping import.");
                return;
            }

            URL url = new URI(orderInfoUrl).toURL();

            log.info("Import BAW OrderInfo codelist");

            CodeList codelist = importFromRegistry(url);
            log.info("Import successful.");
            codelist.setName("BAW OrderInfo");
            codelist.setId(CODELIST_ID);

            this.codeListService.setCodelist(CODELIST_ID, codelist);
            this.codeListService.persistToAll();

        } catch (Exception e) {
            log.error("Problem importing BAW OrderInfo codelist", e);
        }

    }


    public CodeList importFromRegistry(URL url) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderInfoCodelistModel orderInfoCodelist;
        try {
            orderInfoCodelist = objectMapper.readValue(url, OrderInfoCodelistModel.class);
        } catch (Exception e) {
            log.error("Error parsing JSON data from " + url, e);
            throw e;
        }


        List<OrderInfoItem> items = orderInfoCodelist.items();
        CodeList codelist = mapToCodelist(items);

        return codelist;
    }

    private CodeList mapToCodelist(List<OrderInfoItem> items) {
        CodeList codeList = new CodeList();
        List<CodeListEntry> entries = new ArrayList<>();

        for (OrderInfoItem item : items) {
            CodeListEntry entry = new CodeListEntry();
            entry.setId(item.getOrderId());
            String label = item.getOrdernumber() + " - " + item.getJob();
            entry.setField("de", label);
            entries.add(entry);
        }
        codeList.setEntries(entries);
        return codeList;
    }

}
