package com.gamecms.backend.wyd.Configurations.Security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class EndpointConfigurations {

    @Value("${spring.profiles.active}")
    private String profile;
    private final List<String> openApiEndpoints = List.of("/documentacao.html", "/swagger-ui/**", "/v3/api-docs/**");
    private final List<String> openApiUris = List.of("/api/guild/", "/documentacao.html", "/swagger-ui", "/v3/api-docs");

    public List<String> nonProtectedEndpoints = List.of(
            "/api/v1/**",
            "/actuator/**",
            "/api/mercadopago/**",
            "/wydlauncher/**",
            "/staticresources/**",
            "/updater/**",
            "/public/**",
            "/shareddata/**",
            "/notifications/**",
            "/purchases/**"
    );

    public List<String> nonProtectedUris = List.of(
            "/authenticate",
            "/eshop",
            "/guild/guildmark",
            "/noticias",
            "/actuator/prometheus",
            "/mercadopago",
            "/wydlauncher",
            "/staticresources",
            "/updater",
            "/public",
            "/shareddata",
            "/favicon.ico",
            "/notifications",
            "/purchases"
    );

    public List<String> getNonProtectedEndpoints() {
        return profile.equals("development") ? Stream.concat(this.openApiEndpoints.stream(), this.nonProtectedEndpoints.stream()).toList():
                nonProtectedEndpoints;
    }

    public List<String> getNonProtectedUris() {
        return profile.equals("development") ?
                Stream.concat(this.nonProtectedUris.stream(), this.openApiUris.stream()).toList() :
                nonProtectedUris;
    }

    public boolean isPublicEndpoint(HttpServletRequest request) {
        List<String> endpoints = getNonProtectedUris();
        List<String> endpointsMatched = endpoints.stream().filter(n -> request.getRequestURI().contains(n)).toList();
        return !endpointsMatched.isEmpty();
    }


}
