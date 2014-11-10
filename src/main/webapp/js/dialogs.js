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
var clDialogs = {};

//require(["dijit/Dialog"]);
dojo.require("dijit.Dialog");

clDialogs.TYPE_OK           = "OK";
clDialogs.TYPE_ERROR        = "ERROR";

clDialogs.MSG_SAVE_ERROR    = "The Codelist could not be saved due to an error!";
clDialogs.MSG_SAVE_SUCCESS  = "The Codelist has been saved successfully!";
clDialogs.MSG_DELETE_LIST   = "Do you really want to delete the selected codelist? This cannot be undone!";
clDialogs.MSG_CHANGES       = "There are changes which need to be saved first! Do you want to discard these changes?";

clDialogs.showDialog = function(type, content) {
    // remove old one first!
    var myDialog = dijit.byId("codelistDialog");
    if (myDialog) {
        myDialog.set("title", type == "ERROR" ? "Error" : "Information");
        myDialog.set("content", content);
        myDialog.set("class", type == "ERROR" ? "error" : "");
    } else {
        myDialog = new dijit.Dialog({
            id: "codelistDialog",
            // The dialog's title
            title: type == "ERROR" ? "Error" : "Information",
            // The dialog's content
            content: content,
            'class': type == "ERROR" ? "error" : ""
        });
    }
    
    myDialog.show();
};

clDialogs.showAskDialog = function(content) {
    var def = new dojo.Deferred();
    
    var myDialog = dijit.byId("codelistDialogAsk");
    if (myDialog) {
        myDialog.destroyDescendants();
        myDialog.destroy();
    }
    myDialog = new dijit.Dialog({
        id: "codelistDialogAsk",
        // The dialog's title
        title: "Question"
    });
    
    var content = dojo.create("div", {
        "class": "dijitDialogPaneContentArea",
        "innerHTML": content
    }, myDialog.containerNode);
    
    var actionBar = dojo.create("div", {
        "class": "dijitDialogPaneActionBar"
    }, myDialog.containerNode);

    new dijit.form.Button({
        id: "btnOk",
        label: "Ok",
        type: "submit",
        onClick: function() {console.debug("ok clicked"); def.callback();} 
    }).placeAt(actionBar);
    new dijit.form.Button({
        id: "btnCancel",
        label: "Cancel",
        type: "submit",
        onClick: function() {console.debug("cancel clicked");def.cancel();}
    }).placeAt(actionBar);
    
    myDialog.show();
    return def;
};

clDialogs.showDifferencesDialog = function(content) {
    var def = new dojo.Deferred();
    var jsonObject = eval(content)[0];
    
    var content = "<h2>Missing Codelists:</h2><ul>";
    dojo.forEach(jsonObject.missing, function(item) {
        content += "<li><button onclick=\"addFromInitialCodelist("+item.id+", this)\">Add</button>" + item.id + ": " + item.name + "</li>";
    });
    
    
    var myDialog = dijit.byId("codelistDialogDifference");
    if (myDialog) {
        myDialog.destroyDescendants();
        myDialog.destroy();
    }
    myDialog = new dijit.Dialog({
        id: "codelistDialogDifference",
        // The dialog's title
        title: "Differences",
        content: content
    });
    myDialog.show();
    return def;
}
