package com.gamecms.backend.wyd.Controllers;

import com.gamecms.backend.wyd.DTO.ArquivoUpdateResponseDTO;
import com.gamecms.backend.wyd.DTO.UpdaterResponseDTO;
import com.gamecms.backend.wyd.Exceptions.ServerCustomerInativoException;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Models.Updater;
import com.gamecms.backend.wyd.Repositories.UpdaterRepository;
import com.gamecms.backend.wyd.Services.ServerCustomerServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.cj.util.Base64Decoder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/updater/customer/{customerId}")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Updater Controller", description = "Endpoints for Launcher Updater Purposes.")
public class UpdaterController {

    private final ServerCustomerServiceImpl service;

    private final Gson gson;

    private final UpdaterRepository repository;

    @GetMapping(value = "/getfiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArquivoUpdateResponseDTO> getFiles(@PathVariable Long customerId) {
        ServerCustomer customer = service.findById(customerId);
        if(!customer.isActive()){
            throw new ServerCustomerInativoException("getUpdaterVersion - Customer Inativo");
        }

        String url = customer.getDomainIntegration() + "/updater/customer/:customerId/getfiles";
        url = url.replace(":customerId", customerId.toString());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody = Objects.requireNonNull(response.body()).string();
            return gson.fromJson(responseBody, new TypeToken<ArrayList<ArquivoUpdateResponseDTO>>() {
            }.getType());
        } catch (IOException e) {
            log.info("UpdaterController GetFiles - Não foi possivel encaminhar a request - " + "Customer: " + customer.getDomainIntegration() + e.getMessage());
            return null;
        }
    }

    @GetMapping(value = "/getfile/{pathNameEncoded}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long customerId, @PathVariable String pathNameEncoded, HttpServletRequest servletRequest) {
        ServerCustomer customer = service.findById(customerId);
        if(!customer.isActive()){
            throw new ServerCustomerInativoException("getUpdaterVersion - Customer Inativo");
        }
        String url = customer.getDomainIntegration() + "/updater/customer/:customerId/getfile/:filename";
        url = url
                .replace(":customerId", customerId.toString())
                .replace(":filename", pathNameEncoded);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String realPathName = new String(Base64.getDecoder().decode(pathNameEncoded));

            String ipAddress = servletRequest.getHeader("X-Forwarded-For");

            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = servletRequest.getRemoteAddr();
            }

            log.info(" ** UPDATER LAUNCHER - GET FILE ** " + realPathName + " - Ip: " + ipAddress  +" -  CustomerId: " + customerId);

            return  ResponseEntity.ok(response.body().bytes());
        } catch (IOException e) {
            log.info("UpdaterController GetFile - Não foi possivel encaminhar a request - " + "Customer: " + customer.getDomainIntegration() + e.getMessage());
            return null;
        }
    }


    // novos recursos para o self updater
    @GetMapping(value = "/version/{version}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdaterResponseDTO> getUpdaterVersion(@PathVariable Long customerId,@PathVariable Long version) {
            Updater updater = repository.findFirstByIdNotNull();
            return ResponseEntity.ok(UpdaterResponseDTO.builder().version(updater.getVersion()).build());
    }

    @GetMapping()
    public ResponseEntity<byte[]> getUpdaterFile(@PathVariable Long customerId) {
        Updater updater = repository.findFirstByIdNotNull();
        return ResponseEntity.ok(updater.getFile());
    }

}
