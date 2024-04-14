package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.Configurations.Security.ReCaptcha;
import com.gamecms.backend.wyd.DTO.AdminSaveGuildmarkRequestDTO;
import com.gamecms.backend.wyd.DTO.AdminUpdateGuildmarkRequestDTO;
import com.gamecms.backend.wyd.DTO.DynamicTableResponseDTO;
import com.gamecms.backend.wyd.Exceptions.CustomerServerNaoEncontradoException;
import com.gamecms.backend.wyd.Exceptions.GuildmarkNaoEncontradaException;
import com.gamecms.backend.wyd.Exceptions.NaoFoiPossivelReconhecerAGuildmarkException;
import com.gamecms.backend.wyd.Exceptions.SenhaIncorretaException;
import com.gamecms.backend.wyd.Models.Guildmark;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Repositories.GuildmarkRepository;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuildServiceImpl {
    private final GuildmarkRepository repository;
    private final ServerCustomerRepository customerRepository;

    public DynamicTableResponseDTO getDynamicGuildmarkTableByCustomerId(Long customerId) {
        DynamicTableResponseDTO tableResultset = new DynamicTableResponseDTO();
        List<Guildmark> rows = repository.findAllByCustomerId(customerId);
        ;
        tableResultset.setRows(rows);
        tableResultset.setCols(List.of("Id", "Guild Id", "Author", "Image"));
        tableResultset.setEntriesModel(Guildmark.getEntries());
        return tableResultset;
    }

    public void deleteByIdAndCustomerId(Long customerId, String guildId) {
        log.info("deletando Guild: " + guildId + "Customer: " + customerId);
        Guildmark guildmark = repository.findByGuildIdAndCustomerId(guildId, customerId).orElseThrow(() -> new GuildmarkNaoEncontradaException("Guildmark nao encontrada"));
        repository.delete(guildmark);
        log.info("Guild deletada: " + guildId + "Customer: " + customerId);
    }

    public ByteArrayResource findByIdAndCustomerId(String padraoReecebido, Long customerId) {
        String idParseado = padraoReecebido.substring(6).replace(".bmp", "").replaceAll("0", "");
        log.info("Consulta Guildmark: fazendo parse de: " + idParseado);
        if(idParseado.isEmpty()){
            idParseado = "0";
        }
        Guildmark guildmark = repository.findByGuildIdAndCustomerId(idParseado, customerId).orElseThrow(() -> new GuildmarkNaoEncontradaException("Guildmark nao encontrada"));
        log.info("Guildmark encontrada com sucesso: " + guildmark.getGuildId());

        String guildmarkImage = guildmark.getImage().replace("data:", "");
        String base64Image = guildmarkImage.split("base64,")[1];

        byte[] logoBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayResource resource = new ByteArrayResource(logoBytes);

        return resource;
    }


    public void saveByCustomerId(MultipartFile file, String guildId, String username, String captcha, String guildPassword, Long customerId) {
        ReCaptcha.validateCaptchaOrThrow(captcha);

        log.info("Salvando id da guild: " + guildId);
        ServerCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerServerNaoEncontradoException("Server não encontrado na base."));

        Optional<Guildmark> oldGuildmark = repository.findByGuildIdAndCustomerId(guildId, customerId);

        try {
            if (oldGuildmark.isPresent()) {
                Guildmark guildmark = oldGuildmark.get();

                if(!guildmark.getPassword().equals(guildPassword)){
                    throw new SenhaIncorretaException("Upload guildmark - Senha incorreta, contacte a administração.");
                }

                guildmark.setAuthor(username);
                guildmark.setImage("data:image/bmp;base64," + new String(Base64.getEncoder().encode(file.getBytes())));
                repository.save(oldGuildmark.get());
            } else {
                Guildmark guildmark = Guildmark.builder()
                        .customer(customer)
                        .guildId(guildId)
                        .author(username)
                        .image("data:image/bmp;base64," + new String(Base64.getEncoder().encode(file.getBytes())))
                        .dateInc(Date.from(Instant.now()))
                        .password(guildPassword)
                        .build();
                repository.save(guildmark);
            }


        } catch (IOException e) {
            throw new NaoFoiPossivelReconhecerAGuildmarkException("Upload guildmark - Não foi possível ler os bytes da guildmark.");
        }

    }

    public void saveByCustomerId(Long customerId, AdminSaveGuildmarkRequestDTO dto) {
        log.info("Admin Salvando guildmark id da guild: " + dto.getGuildId() + " customer id: " + customerId);


        if (!dto.getImage().contains("image/bmp")) {
            throw new NaoFoiPossivelReconhecerAGuildmarkException("Sua guildmark deve estar em .bmp e ter dimensão de 16x12 pixels.");
        }

        dto.setImage(dto.getImage());

        ServerCustomer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerServerNaoEncontradoException("Server não encontrado na base."));

        Optional<Guildmark> oldGuildmark = repository.findByGuildIdAndCustomerId(dto.getGuildId(), customerId);
        Guildmark guildmark;

        if (oldGuildmark.isPresent()) {
            guildmark = oldGuildmark.get();
            guildmark.setCustomer(oldGuildmark.get().getCustomer());
            guildmark.setGuildId(oldGuildmark.get().getGuildId());
            guildmark.setAuthor(dto.getAuthor());
            guildmark.setImage(dto.getImage());
            guildmark.setPassword(dto.getPassword());
        } else {
            guildmark = Guildmark.builder()
                    .customer(customer)
                    .guildId(dto.getGuildId())
                    .author(dto.getAuthor())
                    .image(dto.getImage())
                    .dateInc(Date.from(Instant.now()))
                    .password(dto.getPassword())
                    .build();
        }
        repository.save(guildmark);
        log.info("Admin salvou guildmark -  id da guild: " + dto.getGuildId() + " customer id: " + customerId);
    }

    public void updateByGuildIdAndByCustomerId(Long customerId, AdminUpdateGuildmarkRequestDTO dto, String oldGuildId) {
        log.info("Admin Atualizando id da guild: " + dto.getGuildId() + " customer id: " + customerId);
        Guildmark guildmark = repository.findByGuildIdAndCustomerId(oldGuildId, customerId).orElseThrow(() -> new GuildmarkNaoEncontradaException(" Não foi possível encontrar essa Guildmark na base."));
        guildmark.setGuildId(dto.getGuildId());
        guildmark.setAuthor(dto.getAuthor());
        dto.setImage(dto.getImage());
        guildmark.setImage(dto.getImage());
        guildmark.setPassword(dto.getPassword());

        repository.save(guildmark);
        log.info("Admin Atualizado com sucesso: " + dto.getGuildId());
    }
}
