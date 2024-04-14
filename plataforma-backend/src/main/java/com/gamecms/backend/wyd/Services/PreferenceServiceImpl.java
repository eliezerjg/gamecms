package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.DTO.DynamicTableResponseDTO;
import com.gamecms.backend.wyd.DTO.UpdatePreferenceRequestDTO;
import com.gamecms.backend.wyd.Exceptions.PreferenceNaoEncontradaException;
import com.gamecms.backend.wyd.Models.Preference;
import com.gamecms.backend.wyd.Repositories.PreferenceRepository;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreferenceServiceImpl {
    private final PreferenceRepository repository;
    private final ServerCustomerRepository customerRepository;


    public DynamicTableResponseDTO getDynamicPreferenceTableByCustomerId(Long customerId){
        List<Preference> rows = repository.findAllByCustomerId(customerId);
        DynamicTableResponseDTO tableResultset = new DynamicTableResponseDTO();
        tableResultset.setRows(rows);
        tableResultset.setCols(List.of("id", "externalreference", "dateinc", "username", "value", "paid", "statusdetail"));
        tableResultset.setEntriesModel(Preference.getEntries());
        return tableResultset;
    }

    public void updatePreferenceByIdAndCustomerId(Long customerId, Long preferenceId, UpdatePreferenceRequestDTO dto){
        log.info("Admin Atualizando Preference: " +preferenceId+ "Customer: " + customerId);
        Preference preference = repository.findByIdAndCustomerId(preferenceId, customerId).orElseThrow(() -> new PreferenceNaoEncontradaException("Preference não encontrada"));
        preference.setPaid(dto.isPaid());
        preference.setUsername(dto.getUsername());
        preference.setValue(dto.getValue());
        repository.save(preference);
        log.info("Admin Preference Atualizada: " +preferenceId+ " Customer: " + customerId);
    }

    public void deletebyIdAndCustomerId(Long customerId, Long preferenceId){
        log.info("Admin  deletando Preference: " +preferenceId+ " Customer: " + customerId);
        Preference preference = repository.findByIdAndCustomerId(preferenceId, customerId).orElseThrow(() -> new PreferenceNaoEncontradaException("Preference não encontrada"));
        repository.delete(preference);
        log.info("Admin Preference deletada: " +preferenceId+ " Customer: " + customerId);
    }

}
