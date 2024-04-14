package com.gamecms.backend.wyd.Configurations.Security;


import com.gamecms.backend.wyd.Models.Whitelist;
import com.gamecms.backend.wyd.Services.WhiteListServiceImpl;
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

import java.util.List;
import java.util.stream.Stream;

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

    private final GeneralFilter jwtAuthFilter;

    private final EndpointConfigurations endpointConfigurations;

    private final WhiteListServiceImpl whiteListService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return decorateFilterChain(http);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PATCH", "DELETE");
            }
        };
    }

    SecurityFilterChain decorateFilterChain(HttpSecurity http) throws Exception {
        List<String> allowedIpBlocks = Stream.concat(staffBlocksIp.stream(), cfAllowedIps.stream()).toList();
        List<String> whiteList = whiteListService.getWhiteList().stream().map(Whitelist::getIp).toList();
        List<String> mergedIps = Stream.concat(allowedIpBlocks.stream(), whiteList.stream()).toList();

        http.authorizeHttpRequests(
                        registry -> mergedIps.forEach(
                                ipBlock -> registry.requestMatchers(new IpAddressMatcher(ipBlock)).permitAll()
                        )

                ).cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable);


        http.authorizeHttpRequests(
                        registry ->
                                registry
                                        .requestMatchers(endpointConfigurations.getNonProtectedEndpoints().toArray(new String[0]))
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}





