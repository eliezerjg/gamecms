package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.Configurations.Security.JwtService;
import com.gamecms.backend.wyd.Configurations.Security.PasswordEncoder;
import com.gamecms.backend.wyd.Configurations.Security.ReCaptcha;
import com.gamecms.backend.wyd.DTO.AdminAuthenticationRequestDTO;
import com.gamecms.backend.wyd.DTO.AdminAuthenticationResponseDTO;
import com.gamecms.backend.wyd.DTO.UpdatedCustomerResponseDTO;
import com.gamecms.backend.wyd.Exceptions.AdminRegisterRequestDTO;
import com.gamecms.backend.wyd.Exceptions.CustomerServerNaoEncontradoException;
import com.gamecms.backend.wyd.Exceptions.SenhaIncorretaException;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl {
    private final ServerCustomerRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthenticationResponseDTO authenticate(AdminAuthenticationRequestDTO request) {
        ReCaptcha.validateCaptchaOrThrow(request.getRecaptcha());

        ServerCustomer user = repository.findServerCustomerByEmail(request.getEmail())
                .orElseThrow(() -> new CustomerServerNaoEncontradoException("User not found"));

        if (!passwordEncoder.getEncoder().matches(request.getPassword(), user.getPassword())) {
            throw new SenhaIncorretaException("Senha incorreta");
        }

        var jwtToken = jwtService.generateToken(request);
        log.info("Authenticate: " + request.getEmail() + " logged, at: " + Date.from(Instant.now()));
        return AdminAuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    public AdminAuthenticationResponseDTO register(AdminRegisterRequestDTO request) {
        ServerCustomer customer = new ServerCustomer();
        customer.setEmail(request.getEmail());
        customer.setUser(request.getUser());
        customer.setPassword(passwordEncoder.getEncoder().encode(request.getPassword()));
        repository.save(customer);

        log.info("Account created: " + request.getEmail() + ", at: " + Date.from(Instant.now()));
        return  AdminAuthenticationResponseDTO.builder().token(jwtService.generateToken(request)).build();
    }


    public UpdatedCustomerResponseDTO updateCustomer(Long id, ServerCustomer updatedCustomer) {
        ServerCustomer customer = repository.findById(id).orElseThrow(() -> new CustomerServerNaoEncontradoException(" Usuario não encontrado"));
        customer.setEmail(updatedCustomer.getEmail());
        log.info("Updated Server Customer: " + updatedCustomer.getEmail() + ", at: " + Date.from(Instant.now()));
        return null;
    }

}
