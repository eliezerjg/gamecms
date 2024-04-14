package com.gamecms.integracao.wyd.Services;

import com.gamecms.integracao.wyd.DTO.ShopProductDTO;
import com.gamecms.integracao.wyd.Exceptions.UsuarioNaoEncontradoException;
import com.gamecms.integracao.wyd.Models.Account;
import com.gamecms.integracao.wyd.Repositories.AccountRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class DonateServiceImpl {

    private final AccountRepository accountRepository;

    private final Gson gson;

    public void importDonateItems(String jsonCartItems, String username){
        log.warn("*** importDonateItems - Request import recebida: " + jsonCartItems);

        List<ShopProductDTO> cartItems = new Gson().fromJson(jsonCartItems, new TypeToken<ArrayList<ShopProductDTO>>(){}.getType());

        cartItems.forEach(item -> {

            if(item.getCategory().equals("DONATE")){
                BigDecimal multipliedValue = item.getValue().multiply(BigDecimal.valueOf(item.getDonateMultiplier()));
                log.info("Importando Donate: " + multipliedValue + " - do usuario: " + username);
                Account account = accountRepository.findByUsername(username)
                        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao encontrado"));

                Long antigoDonate = account.getDonate();
                Long novoDonate = antigoDonate + multipliedValue.longValue();

                account.setDonate(novoDonate);
                accountRepository.save(account);
                log.warn("Importado Donate : antigo donate: "+ antigoDonate + " -  novo donate" + novoDonate + " - do usuario: " + username);
            }else{
                log.warn("Importacao nao implementada: " + item.getTitle() + " - do usuario: " + username);
            }

        });


    }


    public Map<String, String> getByAccount(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao encontrado"));


        log.info("Consultando Donate: " + username + " - donate: " + account.getDonate());
        return Map.of("donate", account.getDonate().toString());

    }

}
