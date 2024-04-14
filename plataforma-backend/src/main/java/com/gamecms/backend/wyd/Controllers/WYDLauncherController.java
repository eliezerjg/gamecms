package com.gamecms.backend.wyd.Controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController()
@RequestMapping("/wydlauncher")
@Slf4j
@Tag(name = "WYDLauncher Controller", description = "Endpoints for WYDLauncher Bypass - Dont bother.")
public class WYDLauncherController {


    @GetMapping(path ="/customer/{customerId}/update1.htm")
    public ResponseEntity<String> getUpdate(@PathVariable Long customerId){
        String update1htm = """               
                64
                [WYD]  \s
                 \s
                5000 default.zip
                                
                NOTIC 598 368 https://back.gamecms.com.br/staticresources/customer/:customerId/wydlauncher
                0               
                """;
        update1htm = update1htm.replace(":customerId", customerId.toString());

        log.info("Launcher apresentacao: Servindo Launcher para cliente " + customerId);
        return ResponseEntity.ok(update1htm);
    }

    @GetMapping(path = "/customer/{customerId}/default.zip", produces = "application/zip")
    public ResponseEntity<byte[]> getDefaultZip() throws IOException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=default.zip");

        ClassPathResource resource = new ClassPathResource("default.zip");
        InputStream inputStream = resource.getInputStream();
        byte[] dados = IOUtils.toByteArray(inputStream);
        log.info("Launcher apresentacao: Download do default.zip ");
        return new ResponseEntity<>(dados, responseHeaders, HttpStatus.OK);
    }
}
