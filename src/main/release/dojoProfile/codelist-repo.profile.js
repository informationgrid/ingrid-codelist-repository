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
