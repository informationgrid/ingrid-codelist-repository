/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2023 wemove digital solutions GmbH
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
package de.ingrid.codelistHandler;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyStarter {
    private static final Logger log = Logger.getLogger(JettyStarter.class);
    
    private static String DEFAULT_WEBAPP_DIR    = "webapp";
    
    private static int    DEFAULT_JETTY_PORT    = 8082;
    

    public static void main(String[] args) throws Exception {
        if (!System.getProperties().containsKey("jetty.webapp"))
            log.warn("Property 'jetty.webapp' not defined! Using default webapp directory, which is '"+DEFAULT_WEBAPP_DIR+"'.");
        if (!System.getProperties().containsKey("jetty.port"))
            log.warn("Property 'jetty.port' not defined! Using default port, which is '"+DEFAULT_JETTY_PORT+"'.");
        
        init();
    }
    
    private static void init() throws Exception {
        WebAppContext webAppContext = new WebAppContext(System.getProperty("jetty.webapp", DEFAULT_WEBAPP_DIR), "/");

        int port = Integer.getInteger("jetty.port", DEFAULT_JETTY_PORT);
        log.info("Start server at port: " + port);
        
        Server server = new Server(port);
        HashUserRealm userRealm = new HashUserRealm("UserRealm");
        webAppContext.getSecurityHandler().setUserRealm(userRealm);
        
        server.setHandler(webAppContext);
        server.start();
    }
}
