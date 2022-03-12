package com.lenovo.accountservice.config;

import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

@Slf4j
@Configuration
public class KeycloakConfig {

    public static final String MASTER_REALM = "master";
    public static final String ADMIN_CLIENT = "admin-cli";

    @Value("${keycloak.auth-server-url}")
    public String authServerUrl;

    @Value("${realm.admin.user}")
    public String username;

    @Value("${realm.admin.password}")
    public String password;

    @Bean
    public Keycloak keycloak() {
        log.info(MessageFormat.format("keycloak authServerUrl: {0}", authServerUrl));
        return KeycloakBuilder.builder()
            .serverUrl(authServerUrl)
            .realm(MASTER_REALM)
            .clientId(ADMIN_CLIENT)
            .username(username)
            .password(password)
            .resteasyClient(resteasyClient())
            .build();
    }

    private ResteasyClient resteasyClient() {
        return new ResteasyClientBuilder().connectionPoolSize(10).build();
    }
}
