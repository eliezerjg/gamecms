package com.gamecms.integracao.wyd.Configurations.Security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public  class EndpointConfigurations {

    @Value("${spring.profiles.active}")
    private String profile;
    private final List<String> openApiEndpoints = List.of("/documentacao.html", "/swagger-ui/**", "/v3/api-docs/**");
    private final List<String> openApiUris = List.of("/api/guild/","/documentacao.html","/swagger-ui","/v3/api-docs");

    public  List<String> nonProtectedEndpoints = List.of(
            "/api/v1/account/**",
            "/api/v1/noticias/**",
             "/api/v1/account/create/**",
            "/api/v1/guild/**",
            "/actuator/**",
            "/api/v1/shop/**",
            "/api/v1/mercadopago/**",
            "/updater/customer/**",
            "/api/v1/donate/**",
            "/api/v1/ranking/**"
    );

    public List<String> nonProtectedUris = List.of(
            "/api/v1/account/login","/api/v1/noticias",
            "/api/v1/account/create","/actuator/prometheus",
            "/api/v1/shop",
            "/api/v1/mercadopago",
            "/updater/customer/",
            "/api/v1/donate",
            "/api/v1/ranking"
    );

    public List<String> getNonProtectedEndpoints(){
        if(profile.equals("development")){
            List<String> developEndpoints = new ArrayList<>();
            developEndpoints.addAll(this.openApiEndpoints);
            developEndpoints.addAll(this.nonProtectedEndpoints);
            return developEndpoints;
        }
        return nonProtectedEndpoints;
    }

    public List<String> getNonProtectedUris(){
        if(profile.equals("development")){
            List<String> developUris = new ArrayList<>();
            developUris.addAll(this.openApiUris);
            developUris.addAll(this.nonProtectedUris);
            return developUris;
        }
        return nonProtectedUris;
    }

    public boolean isPublicEndpoint(HttpServletRequest request){
        List<String> endpoints = getNonProtectedUris();
        List<String> endpointsMatched =  endpoints.stream().filter(n -> request.getRequestURI().contains(n)).toList();
        return !endpointsMatched.isEmpty();
    }


}
