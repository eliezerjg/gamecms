package com.gamecms.backend.wyd.Controllers;


import com.gamecms.backend.wyd.Models.Preference;
import com.gamecms.backend.wyd.Services.PreferenceServiceImpl;
import com.gamecms.backend.wyd.Services.PurchaseServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1/customer/{customerId}/purchases")
@RequiredArgsConstructor
@Tag(name = "PurchasesController Controller", description = "Endpoints for Purchases")
public class PurchasesController {
    private final PurchaseServiceImpl service;

    @GetMapping(path = "/purchase/{externalReference}", produces = "APPLICATION/JSON")
    public ResponseEntity<Map<String, Object>> findPurchase(@PathVariable Long customerId, @PathVariable String externalReference) {
        return ResponseEntity.ok(service.findByExternalReferenceAndCustomerId(externalReference,customerId));
    }

    @GetMapping(path = "/user/{username}", produces = "APPLICATION/JSON")
    public ResponseEntity<List<Map<String, Object>>> findAllPurchasesByUsername(@PathVariable Long customerId, @PathVariable String username) {
        return ResponseEntity.ok(service.findAllByUsername(username,customerId));
    }

}
