package com.gamecms.backend.wyd.Configurations.Security;

import com.gamecms.backend.wyd.DTO.DefaultErrorDTO;
import com.gamecms.backend.wyd.Exceptions.JwtExpiradoException;
import com.gamecms.backend.wyd.Models.Whitelist;
import com.gamecms.backend.wyd.Services.BlacklistServiceImpl;
import com.gamecms.backend.wyd.Services.WhiteListServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeneralFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final EndpointConfigurations endpointConfigurations;
    private final Gson gson;
    private final BlacklistServiceImpl blacklistService;
    private final WhiteListServiceImpl whiteListService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt = Optional.ofNullable(authHeader)
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .orElse(null);

        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        if (whiteListService.getWhiteList().stream().map(Whitelist::getIp).toList().contains(request.getRemoteAddr())) {
            log.warn("White List: request recebida: - METHOD: " + request.getMethod() + " URI: " + request.getRequestURI() + "  Params: " + gson.toJson(request.getParameterMap()));

            whiteListService.save(
                    ipAddress,
                    request.getMethod(),
                    gson.toJson(request.getParameterMap()),
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);
            return;
        }

        if (blacklistService.isBanned(request.getRemoteAddr())) {
            response.sendRedirect("https://gamecms.com.br/");
            log.warn("Banido: Tentativa em excesso a endpoint autenticado: " + request.getRequestURI() + " . auth: " + authHeader + ". JWT: " + jwt + " ip: " + ipAddress);
            return;
        }

        if (endpointConfigurations.isPublicEndpoint(request)) {
            log.info("Info: liberado a endpoint publico: " + request.getMethod() + request.getRequestURI() + " . auth: " + authHeader + ". JWT");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtService.isTokenExpired(jwt)) {
                log.info("Info: liberado a endpoint autenticado: " + request.getMethod() + request.getRequestURI() + " . auth: " + authHeader + ". JWT valido");
                filterChain.doFilter(request, response);
                return;
            }
        } catch (JwtExpiradoException e) {
            log.warn("Erro: negado o acesso a endpoint autenticado: " + request.getMethod() + request.getRequestURI() + " . auth: " + authHeader + ". JWT invalido ou expirado");
            blacklistService.save(
                    ipAddress,
                    request.getMethod(),
                    gson.toJson(request.getParameterMap()),
                    request.getRequestURI()
            );
            DefaultErrorDTO errorResponse = DefaultErrorDTO.builder().message(e.getCustomMessage()).title("Erro").build();
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(errorResponse));
            return;
        }


        filterChain.doFilter(request, response);
    }


}