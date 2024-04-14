package com.gamecms.integracao.wyd.Controllers;


import com.gamecms.integracao.wyd.DTO.ImportCashDTO;
import com.gamecms.integracao.wyd.Services.DonateServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/api/v1/donate")
@RequiredArgsConstructor
@Tag(
        name= "Donate",
        description = "Donate management"
)
@Slf4j
public class DonateController {
    private final DonateServiceImpl service;

    @Value("${gamecms.ip}")
    private String gamecmsIp;

    @Value("${gamecms.backend}")
    private String getGamecmsBackend;

    @PostMapping(path= "/user/{username}")
    public ResponseEntity<Void> create(HttpServletRequest request, @RequestBody String jsonCartItems, @PathVariable String username) {
        String ipAddress = request.getRemoteAddr();

       if(!ipAddress.equals(gamecmsIp) && !ipAddress.equals(getGamecmsBackend)){
           log.warn("Tentativa de uso indevido de criacao de donate, ip: "+ ipAddress);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        service.importDonateItems(jsonCartItems, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Map<String, String>> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.getByAccount(username));
    }

}
