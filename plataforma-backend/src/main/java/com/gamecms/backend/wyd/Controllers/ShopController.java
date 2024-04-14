package com.gamecms.backend.wyd.Controllers;


import com.gamecms.backend.wyd.Models.ShopProduct;
import com.gamecms.backend.wyd.Services.ShopServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/customer/{customerId}/eshop")
@RequiredArgsConstructor
@Tag(name = "ShopProduct Controller", description = "Endpoints for ShopProduct")
public class ShopController {

    private final ShopServiceImpl service;

    @GetMapping()
    public Page<ShopProduct> findAllByCustomerId(@RequestParam int page, @RequestParam int pageSize, @PathVariable Long customerId) {
        return service.findAllByCustomerId(PageRequest.of(page, pageSize), customerId);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<ShopProduct> findByIdAndCustomerId(@PathVariable Long id, @PathVariable Long customerId) {
        return new ResponseEntity<>(service.findItemShopByIdAndCustomerId(id, customerId), HttpStatus.OK);
    }

}
