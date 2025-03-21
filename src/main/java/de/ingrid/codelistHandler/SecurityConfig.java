/*-
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
package de.ingrid.codelistHandler;

import org.eclipse.jetty.util.security.Credential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class SecurityConfig {

    @Value("${credentials.admin:}")
    private List<String> adminUsers;

    @Value("${credentials.user:}")
    private List<String> simpleUsers;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/rest/**").hasAnyRole("admin", "user")
                        .requestMatchers("/**").hasAnyRole("admin")
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("md5", new MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        return new DelegatingPasswordEncoder("noop", encoders);

    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> admins = adminUsers.stream()
                .map(user -> user.split("=>"))
                .map(user -> {
                    String password = Credential.getCredential(user[1]).toString();
                    String type = detectPasswordType(password);
                    return User.withUsername(user[0])
                            .password(type + password)
                            .roles("admin")
                            .build();
                }).toList();

        List<UserDetails> users = simpleUsers.stream()
                .map(user -> user.split("=>"))
                .map(user -> {
                    String password = Credential.getCredential(user[1]).toString();
                    String type = detectPasswordType(password);
                    return User.withUsername(user[0])
                            .password(type + password)
                            .roles("user")
                            .build();
                }).toList();


        return new InMemoryUserDetailsManager(Stream.concat(admins.stream(), users.stream()).collect(Collectors.toList()));
    }

    private static String detectPasswordType(String password) {
        if (password.length() == 60 && (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"))) {
            return "{bcrypt}";
        } else if (password.matches("^[a-fA-F0-9]{32}$")) {
            // MD5-Hash (32-chars hexadecimal)
            return "{md5}";
        } else {
            // plain (for {noop})
            return "{noop}";
        }
    }

}
