/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2022 wemove digital solutions GmbH
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

import de.ingrid.codelistHandler.ChangedCodelistReport;
import de.ingrid.codelistHandler.CodeListManager;
import de.ingrid.codelistHandler.ShortCodelist;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.util.CodeListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;


@RestController
@RequestMapping(path = "/rest/getCodelists/")
public class CodeListAccessResource {
    @Autowired
    private CodeListManager manager;

    // and implement the following GET method 
    @GetMapping(produces = MediaType.APPLICATION_JSON + ";charset=utf-8")
    public ResponseEntity<List<CodeList>> getCodeLists(@QueryParam("lastModifiedDate") String lastModifiedDate, @QueryParam("name") String name) {

        if (name == null || "".equals(name) || "*".equals(name)) {
            return ResponseEntity.ok(manager.getCodeListsAsJson("id", lastModifiedDate, CodeListUtils.SORT_INCREMENT));
        } else {
            return ResponseEntity.ok(manager.getFilteredCodeListsAsJson(name));
        }
    }

    // return only data needed for codelist information (selectbox for administration page)
    @GetMapping(value = "short", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<List<ShortCodelist>> getCodeListsShort(@QueryParam("lastModifiedDate") String lastModifiedDate, @QueryParam("name") String name) {
        if (name == null || "".equals(name) || "*".equals(name)) {
            return ResponseEntity.ok(manager.getCodeListAsShortJson("id", CodeListUtils.SORT_INCREMENT));
        } else {
            return ResponseEntity.ok(manager.getFilteredCodeListsAsShortJson(name));
        }
    }

    @GetMapping(value = "short/{id}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<CodeList> getCodeListShort(@PathVariable("id") String id) {
        return ResponseEntity.ok(manager.getCodeListAsJson(id));
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<CodeList> getCodelist(@PathVariable("id") String id) {
        return ResponseEntity.ok(manager.getCodeListAsJson(id));
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Void> updateCodelists(@PathVariable("id") String id, @RequestBody String body) {
        boolean success = manager.updateCodeList(id, body);

        if (success)
            return ResponseEntity.ok().build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Void> removeCodelist(@PathVariable("id") String id) {
        if (manager.removeCodeList(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(123).build();
        }
    }

    @GetMapping(value = "findEntry/{name}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<List<String[]>> findEntry(@PathVariable("name") String name) {
        return ResponseEntity.ok(manager.findEntry(name.toLowerCase()));
    }

    @GetMapping(value = "checkChanges", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ChangedCodelistReport> checkForChanges() {
        return ResponseEntity.ok(manager.checkChangedInitialCodelist());
    }

    @PutMapping(value = "addInitialCodelist/{id}", produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Void> addInitialCodelist(@PathVariable("id") String id) {
        boolean success = manager.addCodelistFromInitial(id);
        if (success)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(500).build();
    }


    public void setManager(CodeListManager manager) {
        this.manager = manager;
    }

    public CodeListManager getManager() {
        return manager;
    }
}
