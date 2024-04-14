package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.Models.BlackListRequestHistoryItem;
import com.gamecms.backend.wyd.Models.Blacklist;
import com.gamecms.backend.wyd.Models.WhiteListRequestHistoryItem;
import com.gamecms.backend.wyd.Models.Whitelist;
import com.gamecms.backend.wyd.Repositories.WhiteListRequestHistoryRepository;
import com.gamecms.backend.wyd.Repositories.WhitelistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhiteListServiceImpl {
    private final WhitelistRepository repository;
    private final WhiteListRequestHistoryRepository requestHistoryRepository;

    public void save(String ip, String method, String params, String uri) {
        Optional<Whitelist> whitelistItem = repository.findByIp(ip);
        if (whitelistItem.isEmpty()) {
            Whitelist newItem = new Whitelist();
            newItem.setIp(ip);
            Whitelist savedItem = repository.save(newItem);
            WhiteListRequestHistoryItem whiteListRequestHistoryItem = new WhiteListRequestHistoryItem();
            whiteListRequestHistoryItem.setWhiteListItem(savedItem);
            whiteListRequestHistoryItem.setUri(uri);
            whiteListRequestHistoryItem.setMethod(method);
            whiteListRequestHistoryItem.setParams(params);
            requestHistoryRepository.save(whiteListRequestHistoryItem);
            log.warn("White List - adicionado ao Historico (ip novo) - Method: " + method + " Uri: " + uri + " Params: " + params);
            return;
        }

        Whitelist foundedItem = whitelistItem.get();

        WhiteListRequestHistoryItem whiteListRequestHistoryItem = new WhiteListRequestHistoryItem();
        whiteListRequestHistoryItem.setWhiteListItem(foundedItem);
        whiteListRequestHistoryItem.setUri(uri);
        whiteListRequestHistoryItem.setMethod(method);
        whiteListRequestHistoryItem.setParams(params);
        requestHistoryRepository.save(whiteListRequestHistoryItem);
        log.warn("White List "+ (foundedItem.isPaymentMethodNotification() ? "CallBack Pagamento" : "") + " - Adicionado ao Historico (ip existente) - Method:" + method + " Uri: " + uri + " Params: " + params);
    }
    public Optional<Whitelist> findByIp(String ip) {
        return repository.findByIp(ip);
    }

    public List<Whitelist> findAllByIp(String ip) {
        return repository.findAllByIp(ip);
    }

    public List<Whitelist> getWhiteList() {
        return repository.findAll();
    }


}
