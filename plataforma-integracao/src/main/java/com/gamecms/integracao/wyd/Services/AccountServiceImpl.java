package com.gamecms.integracao.wyd.Services;

import com.gamecms.integracao.wyd.Configurations.Security.JwtService;
import com.gamecms.integracao.wyd.Configurations.Security.ReCaptcha;
import com.gamecms.integracao.wyd.FileModels.Account;
import com.gamecms.integracao.wyd.DTO.ChangePassRequestDTO;
import com.gamecms.integracao.wyd.DTO.CreateAccountRequestDTO;
import com.gamecms.integracao.wyd.DTO.LoginAcountRequestDTO;
import com.gamecms.integracao.wyd.Exceptions.*;
import com.gamecms.integracao.wyd.Repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl {

    @Value("${ingame}")
    private String inGamePath;

    @Value("${dbsrv}")
    private String dbSrvPath;

    @Value("${common}")
    private String commonPath;

    @Value("${user}")
    private String importUserPath;

    private final JwtService jwtService;

    private final AccountRepository repository;



    public CreateAccountRequestDTO create(CreateAccountRequestDTO dtoRecebido) throws CaptchaInvalidoException {

        ReCaptcha.validateCaptchaOrThrow(dtoRecebido);
        String dir = Account.getRealPath(inGamePath, dbSrvPath, dtoRecebido);

        Optional<com.gamecms.integracao.wyd.Models.Account> conta = repository.findByUsername(dtoRecebido.getUser());

        if (conta.isPresent()) {
            log.error("Erro - Essa conta já existe. caminho:" + dir + ".");
            throw new ContaJaExisteException("Conta já existe.");
        }

        com.gamecms.integracao.wyd.Models.Account novaConta = com.gamecms.integracao.wyd.Models.Account
                .builder()
                .user_id(1)
                .username(dtoRecebido.getUser())
                .password(dtoRecebido.getPassword())
                .created_at(Date.from(Instant.now()))
                .updated_at(null)
                .donate(0L)
                .online(0)
                .numerica(000000)
                .divina(0)
                .Pix("0")
                .build();

        repository.save(novaConta);

        log.info("Conta Criada com sucesso: " + dtoRecebido.getUser());

        return dtoRecebido;
    }


    public LoginAcountRequestDTO login(LoginAcountRequestDTO dtoRecebido) throws CaptchaInvalidoException{

        ReCaptcha.validateCaptchaOrThrow(dtoRecebido);
        Optional<com.gamecms.integracao.wyd.Models.Account> conta = repository.findByUsername(dtoRecebido.getUser());

        if (conta.isEmpty()) {
            log.error("Erro - Essa conta não existe. caminho:");
            throw new UsuarioNaoEncontradoException("Esse usuário não existe.");
        }


        String password = conta.get().getPassword();

        if (!password.equals(dtoRecebido.getPassword())) {
                log.error("Erro - Login Senha incorreta.");
                throw new SenhaIncorretaException("Senha incorreta.");
        }

        dtoRecebido.setJwt(jwtService.generateToken(dtoRecebido));

        log.info("Usuario logado com sucesso: " + dtoRecebido.getUser());
        return dtoRecebido;
    }

    public ChangePassRequestDTO changePass(ChangePassRequestDTO dtoRecebido) throws CaptchaInvalidoException {
        ReCaptcha.validateCaptchaOrThrow(dtoRecebido);
        Optional<com.gamecms.integracao.wyd.Models.Account> conta = repository.findByUsername(dtoRecebido.getUser());

            if (conta.isEmpty()) {
                log.error("Erro - Essa conta não existe. caminho:");
                throw new UsuarioNaoEncontradoException("Esse usuário não existe.");
            }

            String password = conta.get().getPassword();

            if(!dtoRecebido.getPassword().equals(password)){
                throw new SenhaIncorretaException("Senha incorreta");
            }

            conta.get().setPassword(dtoRecebido.getNewPassword());
            repository.save(conta.get());

            log.info("Senha alterada com sucesso - user:" + dtoRecebido.getUser() + " - nova senha:" + dtoRecebido.getNewPassword());
            return dtoRecebido;
    }



}
