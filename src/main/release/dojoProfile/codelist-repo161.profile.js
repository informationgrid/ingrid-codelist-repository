/*
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
