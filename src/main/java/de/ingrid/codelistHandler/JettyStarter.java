package de.ingrid.codelistHandler;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
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

        Server server = new Server(Integer.getInteger("jetty.port", DEFAULT_JETTY_PORT));
        server.setHandler(webAppContext);
        server.start();
    }
}
