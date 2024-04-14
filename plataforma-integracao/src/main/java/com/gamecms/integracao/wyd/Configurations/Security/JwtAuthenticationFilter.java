package com.gamecms.integracao.wyd.Configurations.Security;

import com.gamecms.integracao.wyd.Exceptions.JwtExpiradoException;
import com.google.gson.Gson;
import com.gamecms.integracao.wyd.DTO.DefaultErrorDTO;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final EndpointConfigurations endpointConfigurations;

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

        if (IPsBlacklist.getInstance().containsIP(request.getRemoteAddr()) && IPsBlacklist.getInstance().getIPCount(request.getRemoteAddr()) >= 15) {
            response.sendRedirect("https://gamecms.com.br/404.html");
            log.error("Banido: Tentativa em excesso a endpoint autenticado: " + request.getRequestURI() + ". auth: "+ authHeader+ ". JWT: " + jwt + " ip: " + request.getRemoteAddr());
            return;
        }

        if(endpointConfigurations.isPublicEndpoint(request)){
            log.info("Info: liberado a endpoint publico: " + request.getRequestURI() + ". auth: "+ authHeader+ ". JWT");
            filterChain.doFilter(request, response);
            return;
        }

        try{
            if (jwtService.isTokenExpired(jwt)) {
                log.info("Info: liberado a endpoint autenticado: " + request.getRequestURI() + ". auth: "+ authHeader+ ". JWT valido");
                filterChain.doFilter(request, response);
                return;
            }
        } catch(JwtExpiradoException e){
            log.info("Erro: negado o acesso a endpoint autenticado: " + request.getRequestURI() + ". auth: "+ authHeader+ ". JWT invalido ou expirado");
            IPsBlacklist.getInstance().addIP(request.getRemoteAddr());

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