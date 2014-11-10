/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 wemove digital solutions GmbH
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
/* VALIDATORS */
var errorMessages = [];

function isValid() {
    errorMessages = [];
    var def = new dojo.Deferred();
    var result = listIdTextbox.validate() && (dojo.style("entryContent", "display") == "none" || (entryIdTextbox.validate() && localisationsValid()));
    dojo.when(listIdExists(listIdTextbox), function(isValid) {
        if (!isValid) {
            result = false;
            listIdTextbox.validate();
        }
        if (result) def.resolve();
        else def.cancel();
    });
    return def;
}

function localisationsValid() {
    var result = true;
    
    entryLocalisationGrid.store.fetch({
        onItem: function(item) {
            if (item.lang == "" || item.lang == "?") { // || item.value == "?")
                result = false;
                errorMessages.push("Locale is undefined but has to be set!");
            }
        }
    });
    
    return result;
}

function idExists(newId) {
    var invalid = false;

    // id not empty!
    if (newId == "") invalid = true;

    // get old id from selected entry
    var selItem = entriesGrid.getItem(entriesGrid.selection.selectedIndex);
    entriesGrid.store.fetch({
        onItem : function(item) {
            if (item != selItem) {
                if (item.id == newId) {
                    invalid = true;
                    errorMessages.push("Entry-ID already exists! Please choose a unique ID!");
                }
            }
        }
    });

    if (invalid) {
        entriesGridStandby.show();
        toggleCodelistSelection(true);
    } else {
        entriesGridStandby.hide();
        toggleCodelistSelection(false);
    }

    return invalid;
}

function listIdValidator(newId) {
    if (forceInvalidListIdOnce) {
        forceInvalidListIdOnce = false;
        return false;
    }

    var isValid = true;

    // id not empty!
    if (newId == "") {
        isValid = false;
        errorMessages.push("List-ID is empty!");
    }

    return isValid;
}

function listIdExists(newId) {
    var defRet = new dojo.Deferred();
    var isValid = true;

    // if a new id was chosen for the codelist
    if (currentCodelist != newId) {
        dojo.when(fetchCodelistData(newId), function(codelist) {
            if (codelist.id != undefined) {
                isValid = false;
                errorMessages.push("List-ID already exists. Please choose a unique ID!");
                forceInvalidListIdOnce = true;
            }
            defRet.resolve(isValid);
        });
    } else {
        defRet.resolve(isValid);
    }

    /*
     * // works only if select box was opened at least once!!! var
     * codelistOptions =
     * dojo.query('#dijit_form_FilteringSelect_0_popup')[0].children;
     * dojo.forEach(codelistOptions, function(option) {
     * console.debug(option.innerHTML); if (option.innerHTML.indexOf(newId+" ")
     * === 0 && clSelect.get("value") != newId) { invalid = true; return; } });
     */

    // return invalid;
    return defRet;
}
