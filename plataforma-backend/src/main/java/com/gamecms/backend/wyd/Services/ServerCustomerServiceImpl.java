package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.DTO.AdminMinhaContaUpdateDTO;
import com.gamecms.backend.wyd.Exceptions.CustomerServerNaoEncontradoException;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServerCustomerServiceImpl {
    private final ServerCustomerRepository repository;

    public ServerCustomer findById(Long customerId){
        return repository.findById(customerId).orElseThrow(() -> new CustomerServerNaoEncontradoException("Server não encontrado"));
    }


    public void updateById(Long customerId, AdminMinhaContaUpdateDTO dto){
        log.info("Admin - alterando dados de configuração - customer: " + customerId);
        ServerCustomer customer = repository.findById(customerId).orElseThrow(() -> new CustomerServerNaoEncontradoException("Server não encontrado"));
        customer.setEmail(dto.getEmail());
        customer.setDatabaseName(dto.getDatabaseName());
        customer.setServerIpv4Address(dto.getServerIpv4Address());
        customer.setServerIpv6Address(dto.getServerIpv6Address());
        customer.setSshKey(dto.getSshKey());
        customer.setDomainFrontEnd(dto.getDomainFrontEnd());
        customer.setDomainIntegration(dto.getDomainIntegration());
        customer.setServerFantasyName(dto.getServerFantasyName());
        customer.setReleaseDate(dto.getReleaseDate());
        customer.setEnableReleaseDateCounter(dto.isEnableReleaseDateCounter());
        customer.setLauncherImage(dto.getLauncherImage());
        customer.setLogoImage(dto.getLogoImage());
        customer.setBackgroundImage(dto.getBackgroundImage());
        customer.setHomePageText(dto.getHomePageText());
        customer.setFooterText(dto.getFooterText());
        customer.setDiscordUrl(dto.getDiscordUrl());
        customer.setFacebookUrl(dto.getFacebookUrl());
        customer.setYoutubeUrl(dto.getYoutubeUrl());
        customer.setWhatsappUrl(dto.getWhatsappUrl());
        customer.setReleaseImage(dto.getReleaseImage());
        customer.setReleaseMessage(dto.getReleaseMessage());
        customer.setHomePageTitle(dto.getHomePageTitle());
        customer.setFavIconImage(dto.getFavIconImage());
        repository.save(customer);
        log.info("Admin - alterou dados de configuração com sucesso - customer: " + customerId);
    }

}
