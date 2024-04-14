package com.gamecms.integracao.wyd.Services;

import com.gamecms.integracao.wyd.Models.Account;
import com.gamecms.integracao.wyd.Models.Character;
import com.gamecms.integracao.wyd.Repositories.AccountRepository;
import com.gamecms.integracao.wyd.Repositories.CharacterRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterServiceImpl {

    private final CharacterRepository repository;
    private final AccountRepository accountRepository;
    private final Gson gson;

    public String findFirst100ByFrags() {
        List<Character> topRankings = repository.findTop100ByOrderByFragsDesc();
        Map<String, String> topRankingCharactersOnline = new HashMap<>();

        topRankings.forEach(n -> {
            Account conta = accountRepository.findById(Long.valueOf(n.getAccountId())).get();
            topRankingCharactersOnline.put(n.getNick(), String.valueOf(conta.getOnline()));
        });

        Map<String, Object> resultMap = Map.of(
                "rankingData",  repository.findTop100ByOrderByFragsDesc(),
                "onlineData" , topRankingCharactersOnline
        );

        String result = gson.toJson(resultMap);
        return result;
    }

    public String findFirst5ByFrags() {
        List<Character> topRankings = repository.findTop5ByOrderByFragsDesc();
        Map<String, String> topRankingCharactersOnline = new HashMap<>();

        topRankings.forEach(n -> {
            Account conta = accountRepository.findById(Long.valueOf(n.getAccountId())).get();
            topRankingCharactersOnline.put(n.getNick(), String.valueOf(conta.getOnline()));
        });

        Map<String, Object> resultMap = Map.of(
                "rankingData",  repository.findTop100ByOrderByFragsDesc(),
                "onlineData" , topRankingCharactersOnline
        );

        String result = gson.toJson(resultMap);
        return result;
    }

}
