package com.gamecms.backend.wyd.Controllers;

import com.gamecms.backend.wyd.Services.MercadoPagoServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/customer/{customerId}/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications Controller", description = "Endpoints for webhook notifications")
public class NotificationsController {
    private final MercadoPagoServiceImpl service;

    @PostMapping(path = "/mercadopago/updatePreference/{externalReference}")
    public void updatePreference(@PathVariable String externalReference, @PathVariable Long customerId, HttpServletRequest servletRequest){
        service.updatePreference(externalReference, customerId, servletRequest);
    }
}
