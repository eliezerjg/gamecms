package com.gamecms.backend.wyd.Controllers;

import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Services.ServerCustomerServiceImpl;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/v1/customer/{customerId}/public")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Public Configurations For FrontEnd Controller", description = "Endpoints for Serving public configurations for dynamic frontend.")
public class PublicConfigurationsForFrontEndController {

    private final ServerCustomerServiceImpl serverService;
    private final Gson gson;

    // TODO: Recurso repetido rever (publicconfigurationsforfrontendcontroller)
    @GetMapping(path ="/configuracoes")
    public String getSiteParams(@PathVariable Long customerId){
        log.info("Recarregando cache parametros Frontend customerId: " + customerId );
        ServerCustomer customer = serverService.findById(customerId);
        customer.setPassword(null);
        customer.setEmail(null);
        customer.setSshKey(null);
        customer.setDatabaseName(null);
        customer.setId(null);
        customer.setUser(null);
        customer.setServerIpv4Address(null);
        customer.setServerIpv6Address(null);
        log.info("Carregado com sucesso cache parametros Frontend customerId: " + customerId );
        return gson.toJson(customer);
    }

    @GetMapping(path = "/getLogo")
    public ResponseEntity<Resource> getSiteLogo(@PathVariable Long customerId) {
        ServerCustomer customer = serverService.findById(customerId);

        String logoImage = customer.getLogoImage().replace("data:", "");
        String contentType = logoImage.split(";")[0];
        String base64Image = logoImage.split("base64,")[1];

        byte[] logoBytes = Base64.getDecoder().decode(base64Image);
        ByteArrayResource resource = new ByteArrayResource(logoBytes);

        return ResponseEntity.ok()
                .contentLength(logoBytes.length)
                .header("Content-type", contentType)
                .header("Content-disposition", "inline; filename=logo.jpg")
                .body(resource);
    }

}
