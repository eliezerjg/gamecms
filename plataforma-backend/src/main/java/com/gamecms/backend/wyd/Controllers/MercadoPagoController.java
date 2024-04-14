package com.gamecms.backend.wyd.Controllers;


import com.gamecms.backend.wyd.Services.MercadoPagoServiceImpl;
import com.mercadopago.resources.preference.Preference;
import com.gamecms.backend.wyd.DTO.CreatePreferenceRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/customer/{customerId}/mercadopago")
@RequiredArgsConstructor
@Tag(name = "MercadoPago Controller", description = "Endpoints for Payment Gateway Integration")
public class MercadoPagoController {
    private final MercadoPagoServiceImpl service;

    @PostMapping(path = "/createPreference")
    public ResponseEntity<Preference> createPreference(@RequestBody CreatePreferenceRequestDTO requestDTO, @PathVariable Long customerId) {
        return new ResponseEntity<>(service.createPreference(requestDTO,customerId), HttpStatus.CREATED);
    }

}
