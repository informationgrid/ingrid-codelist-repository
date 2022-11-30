package de.ingrid.codelistHandler;

import org.apache.log4j.Logger;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    private Logger log = Logger.getLogger(SecurityConfig.class);

    @Value("${jetty.base.resources:src/main/webapp,target}")
    private String[] jettyBaseResources;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable();
        /*        .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic();*/

        return http.build();
    }

/*    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User
                .withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }*/


    @Bean
    WebServerFactoryCustomizer embeddedServletContainerCustomizer(final JettyServerCustomizer jettyServerCustomizer) {

        return container -> {
            if (container instanceof JettyServletWebServerFactory) {
                log.info("Adding jetty server customizer");
                ((JettyServletWebServerFactory) container).addServerCustomizers(jettyServerCustomizer);
            }
        };
    }

    @Bean
    JettyServerCustomizer jettyServerCustomizer(final LoginService loginService, ConstraintSecurityHandler constraintSecurityHandler) {
        return server -> {
            log.info("Setting loginService");
            ((WebAppContext) server.getHandler()).setSecurityHandler(constraintSecurityHandler);
            ((WebAppContext) server.getHandler()).setBaseResource(new ResourceCollection(jettyBaseResources));
        };
    }

    @Bean
    ConstraintSecurityHandler constraintSecurityHandler(final LoginService loginService) {
        final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

        securityHandler.setLoginService(loginService);

        Constraint constraint = new Constraint();
        constraint.setName("Auth");
        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);
        securityHandler.addConstraintMapping(mapping);
        securityHandler.setLoginService(loginService);

        return securityHandler;
    }

    @Bean
    LoginService loginService() {
        HashLoginService loginService = new HashLoginService("InGrid HashLogin Service");
        loginService.setConfig("./src/develop/resources/realm.properties");
        return loginService;
    }
}