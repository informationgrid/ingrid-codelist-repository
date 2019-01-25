/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2019 wemove digital solutions GmbH
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

import de.ingrid.codelists.model.CodeListEntry;

/**
 * The model for a CodeList, which is used for updated codelists.
 * It introduces a new field which tells the update process what
 * to do with this codelist.
 * 
 * @author André Wallat
 *
 */
public class CodeListEntryUpdate {
    
    public static enum Type { UPDATE, REMOVE, ADD };
    
    private Type type;
    
    private CodeListEntry entry;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public CodeListEntry getEntry() {
        return entry;
    }

    public void setEntry(CodeListEntry entry) {
        this.entry = entry;
    }
}
