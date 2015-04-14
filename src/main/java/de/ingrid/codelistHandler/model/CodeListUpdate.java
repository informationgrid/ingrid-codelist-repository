/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2015 wemove digital solutions GmbH
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
package de.ingrid.codelistHandler.model;

import java.util.List;

import de.ingrid.codelists.model.CodeList;

/**
 * The model for a CodeList, which is used for updated codelists.
 * It introduces a new field which tells the update process what
 * to do with this codelist.
 * 
 * @author André Wallat
 *
 */
public class CodeListUpdate extends CodeList {
    
    public static enum Type { UPDATE, REMOVE, ADD, ENTRYUPDATE };
    
    private Type type;
    
    private List<CodeListEntryUpdate> updateEntries;
    
    private CodeList codelist;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<CodeListEntryUpdate> getUpdateEntries() {
        return updateEntries;
    }

    public void setUpdateEntries(List<CodeListEntryUpdate> updateEntries) {
        this.updateEntries = updateEntries;
    }

    public CodeList getCodelist() {
        return codelist;
    }

    public void setCodelist(CodeList codelist) {
        this.codelist = codelist;
    }
    

    
}
