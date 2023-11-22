/*-
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
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private Logger log = Logger.getLogger(SecurityConfig.class);

    @Value("${jetty.base.resources:public}")
    private String[] jettyBaseResources;
    
    @Value("${credentials.admin:}")
    private List<String> adminUsers;
    
    @Value("${credentials.user:}")
    private List<String> simpleUsers;

    @Bean
    WebServerFactoryCustomizer embeddedServletContainerCustomizer(final JettyServerCustomizer jettyServerCustomizer) {

        return container -> {
            if (container instanceof JettyServletWebServerFactory) {
                ((JettyServletWebServerFactory) container).addServerCustomizers(jettyServerCustomizer);
            }
        };
    }

    @Bean
    JettyServerCustomizer jettyServerCustomizer(ConstraintSecurityHandler constraintSecurityHandler) {


        return server -> {
            ((WebAppContext) server.getHandler()).setBaseResource(new ResourceCollection(jettyBaseResources));
            ((WebAppContext) server.getHandler()).setSecurityHandler(constraintSecurityHandler);
        };
    }

    @Bean
    ConstraintSecurityHandler constraintSecurityHandler() {
        final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

        HashLoginService loginService = new HashLoginService("InGrid Realm");
        
        UserStore userStore = new UserStore();
        addUsersToStore(userStore, adminUsers, "admin");
        addUsersToStore(userStore, simpleUsers, "user");

        loginService.setUserStore(userStore);
        securityHandler.setLoginService(loginService);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"admin"});
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setConstraint(constraint);
        constraintMapping.setPathSpec("/*");

        Constraint constraintRest = new Constraint();
        constraintRest.setName(Constraint.__BASIC_AUTH);
        constraintRest.setRoles(new String[]{"admin", "user"});
        constraintRest.setAuthenticate(true);
        
        ConstraintMapping constraintMappingRest = new ConstraintMapping();
        constraintMappingRest.setConstraint(constraintRest);
        constraintMappingRest.setPathSpec("/rest/*");

        securityHandler.addConstraintMapping(constraintMapping);
        securityHandler.addConstraintMapping(constraintMappingRest);
        securityHandler.setLoginService(loginService);

        BasicAuthenticator authenticator = new BasicAuthenticator();
        securityHandler.setAuthenticator(authenticator);

        return securityHandler;
    }

    private void addUsersToStore(UserStore userStore, List<String> users, String role) {
        for (String user : users) {
            String[] split = user.split("=>");
            userStore.addUser(split[0], Credential.getCredential(split[1]), new String[]{role});
        }
    }

}
