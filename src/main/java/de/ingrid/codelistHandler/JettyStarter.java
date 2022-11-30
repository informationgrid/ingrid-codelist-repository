/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2022 wemove digital solutions GmbH
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
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@ImportResource({"/application-context.xml"})
@SpringBootApplication
@EnableWebSecurity
public class JettyStarter {
    private static final Logger log = Logger.getLogger(JettyStarter.class);
    
    private static int    DEFAULT_JETTY_PORT    = 8082;
    

    public static void main(String[] args) throws Exception {
        if (!System.getProperties().containsKey("jetty.port"))
            log.warn("Property 'jetty.port' not defined! Using default port, which is '"+DEFAULT_JETTY_PORT+"'.");
        
//        init();
        SpringApplication.run(JettyStarter.class, args);
    }


/*    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
//                .authenticationEntryPoint(authenticationEntryPoint);
//                .logout()
//                .logoutUrl("/perform_logout")
//                .deleteCookies("JSESSIONID")
//                .and().authenticationManager(authManager);
//        http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }*/

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
    
    /*private static void init() throws Exception {
        WebAppContext webAppContext = new WebAppContext(System.getProperty("jetty.webapp", DEFAULT_WEBAPP_DIR), "/");

        int port = Integer.getInteger("jetty.port", DEFAULT_JETTY_PORT);
        log.info("Start server at port: " + port);
        
        Server server = new Server(port);
        HashUserRealm userRealm = new HashUserRealm("UserRealm");
        webAppContext.getSecurityHandler().setUserRealm(userRealm);
        
        server.setHandler(webAppContext);
        server.start();
    }*/
}
