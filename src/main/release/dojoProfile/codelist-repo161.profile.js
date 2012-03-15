dependencies = {
    layers: [
    
        {
        name: "../../custom/layer.js",
        resourceName: "custom.layer",
        dependencies: ["custom.layer"]
    
        }
    ],
    
    prefixes: [
        //["dojo", "../../../../target/js/dojo/dojo-1.7.2/dojo"],
        ["dijit", "../dijit"],
        ["dojox", "../dojox"],
        ["custom", "../custom"]
    ]
}