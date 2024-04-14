package com.gamecms.backend.wyd.Services;

import com.gamecms.backend.wyd.Exceptions.CustomerServerNaoEncontradoException;
import com.gamecms.backend.wyd.Exceptions.ImagemNaoEncontradaException;
import com.gamecms.backend.wyd.Models.Image;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Repositories.ImageRepository;
import com.gamecms.backend.wyd.Repositories.ServerCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl {
    private final ImageRepository repository;
    private final ServerCustomerRepository customerRepository;

    public Image findByIdAndCustomerId(String id, Long customerId){
        return repository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new ImagemNaoEncontradaException("Image not found"));
    }

    public void save(String receivedImage, Long customerId){
        log.info("Imagem - Cliente: " + customerId + " - Salvando imagem.");
        Image image = new Image();
        image.setImage(receivedImage);
        ServerCustomer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerServerNaoEncontradoException("Server not found"));
        image.setCustomer(customer);
        repository.save(image);
        log.info("Imagem - Cliente: " + customerId + " - Salvo com sucesso.");
    }

    public void deleteByIdAndCustomerId(String id, Long customerId){
        log.info("Imagem - Cliente: " + customerId + " - deletando imagem, id: " + id);
        Image image = repository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new ImagemNaoEncontradaException("Image not found"));
        repository.delete(image);
        log.info("Imagem - Cliente: " + customerId + " - deletada com sucesso, id:" + id);
    }

    public void updateByIdAndCustomerId(String id, Long customerId, String receivedImage){
        log.info("Imagem - Cliente: " + customerId + " - atualizando imagem, id: " + id);
        Image image = repository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new ImagemNaoEncontradaException("Image not found"));
        image.setImage(receivedImage);
        repository.save(image);
        log.info("Imagem - Cliente: " + customerId + " - atualizada com sucesso, id:" + id);
    }

}
