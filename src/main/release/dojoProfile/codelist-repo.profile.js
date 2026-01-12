/*
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
//dependencies = {
//    layers: [
//        {
//            name: "dojo.js",
//            dependencies: [
//            ]
//        },{
//        // This is a specially named layer, literally 'dojo.js'
//        // adding dependencies to this layer will include the modules
//        // in addition to the standard dojo.js base APIs. 
//        name: "mydojo.js",
//        layerDependencies:
//            [
//            "dojo.js",
//            ],
//        dependencies: [
//         "dojo.selector.acme",
//         "dojox.grid.DataGrid",
//         "dojo.data.ItemFileReadStore",
//         "dojo.data.ItemFileWriteStore",
//         "dijit.layout.ContentPane",
//         "dijit.layout.BorderContainer",
//         "dojo.store.JsonRest"
//        ]
//    }],
//    
//    prefixes: [
//        //["dojo", "../../../../target/js/dojo/dojo-1.7.2/dojo"],
//        ["dijit", "../dijit"],
//        ["dojox", "../dojox"]
//    ]
//}

var profile = {      
    basePath: "..", 
    layerOptimize: "shrinksafe.keepLines", 
    releaseDir: "../release",
    hasReport: true,

    packages:[ 
        { 
            name: "dojo", 
            location: "../../dojo" 
        }, 
        { 
            name: "dijit", 
            location: "../../dijit" 
        },
        { 
            name: "dojox", 
            location: "../../dojox" 
        },
        { 
            name: "codelist-repo", 
            location: "../../../../../../src/main/webapp/js"
        }
    ], 

    layers: {             
        "codelist-repo/main": { 
            include: [ 
                "dojox/grid/DataGrid", "dijit/Dialog", "dijit/form/ValidationTextBox", "dijit/form/Button",
                "dijit/form/FilteringSelect", "dijit/Toolbar", "dijit/form/NumberTextBox", "dojo/data/ItemFileWriteStore"
            ],
            exclude: [ ]
        }
    }
};
