

package com.gamecms.integracao.wyd.Configurations.Security;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final List<String> cfAllowedIps = CloudflareIPRangeFetcherSingleton.getInstance().getIPRanges();
    private final List<String> staffBlocksIp = List.of(
            "5.161.56.62/32",
            "127.0.0.1/24",
            "192.168.1.1/24"
    );

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final EndpointConfigurations endpointConfigurations;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return decorateFilterChain(http);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET","POST", "PATCH");
            }
        };
    }

     SecurityFilterChain decorateFilterChain(HttpSecurity http) throws Exception {
        List<String> allowedIpBlocks = new ArrayList<>();
        allowedIpBlocks.addAll(staffBlocksIp);
        allowedIpBlocks.addAll(cfAllowedIps);

        http.authorizeHttpRequests(
                registry -> allowedIpBlocks.forEach(
                        bloco -> registry.requestMatchers(new IpAddressMatcher(bloco)).permitAll()
                )
        );


        http.authorizeHttpRequests(
                        registry ->
                                registry
                                        .requestMatchers(endpointConfigurations.getNonProtectedEndpoints().toArray(new String[0]))
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());

        return http.build();
    }
}





