package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.Models.Blacklist;
import com.gamecms.backend.wyd.Models.BlackListRequestHistoryItem;
import com.gamecms.backend.wyd.Repositories.BlacklistRepository;
import com.gamecms.backend.wyd.Repositories.RequestHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistServiceImpl {
    private final BlacklistRepository repository;
    private final RequestHistoryRepository requestHistoryRepository;
    private static final int MAX_ATTEMPS = 15;


    public List<Blacklist> getBlackList() {
        List<Blacklist> ipList = repository.findAll();

        return ipList.stream()
                .filter(blacklistItem -> requestHistoryRepository.findAllByBlackListItem(blacklistItem).size() >= MAX_ATTEMPS)
                .collect(Collectors.toList());
    }

    public boolean isBanned(String ip){
        Optional<Blacklist> blacklistItem = repository.findByIp(ip);
        return blacklistItem.isEmpty() ? false : requestHistoryRepository.findAllByBlackListItem(blacklistItem.get()).size() >= MAX_ATTEMPS;
    }

    public void save(String ip, String method, String params, String uri) {
        Optional<Blacklist> blacklistItem = repository.findByIp(ip);
        if (blacklistItem.isEmpty()) {
            Blacklist newItem = new Blacklist();
            newItem.setIp(ip);
            Blacklist savedItem = repository.save(newItem);
            BlackListRequestHistoryItem blackListRequestHistoryItem = new BlackListRequestHistoryItem();
            blackListRequestHistoryItem.setBlackListItem(savedItem);
            blackListRequestHistoryItem.setUri(uri);
            blackListRequestHistoryItem.setMethod(method);
            blackListRequestHistoryItem.setParams(params);
            requestHistoryRepository.save(blackListRequestHistoryItem);
            log.warn("Adicionado ao Historico (ip novo) - Method: " + method + " Uri: " + uri + " Params: " + params);
            return;
        }

        Blacklist foundedItem = blacklistItem.get();
        if(requestHistoryRepository.findAllByBlackListItem(foundedItem).size() >= MAX_ATTEMPS){
            log.warn("Ip banido em definitivo: " + ip + " retornando");
            return;
        }
        BlackListRequestHistoryItem blackListRequestHistoryItem = new BlackListRequestHistoryItem();
        blackListRequestHistoryItem.setBlackListItem(foundedItem);
        blackListRequestHistoryItem.setUri(uri);
        blackListRequestHistoryItem.setMethod(method);
        blackListRequestHistoryItem.setParams(params);
        requestHistoryRepository.save(blackListRequestHistoryItem);
        log.warn("Adicionado ao Historico (ip existente) - Method:" + method + " Uri: " + uri + " Params: " + params);
    }


}
