package de.ingrid.codelistHandler;

import org.apache.log4j.Logger;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    private Logger log = Logger.getLogger(SecurityConfig.class);

    @Value("${jetty.base.resources:public}")
    private String[] jettyBaseResources;

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

        HashLoginService loginService = new HashLoginService("InGrid Realm", "./src/develop/resources/realm.properties");
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

}