package com.gamecms.backend.wyd.Controllers;

import com.gamecms.backend.wyd.DTO.*;
import com.gamecms.backend.wyd.Exceptions.CustomerServerNaoEncontradoException;
import com.gamecms.backend.wyd.Models.Noticia;
import com.gamecms.backend.wyd.Models.Purchase;
import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Services.*;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api/v1/customer/{customerId}/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "Endpoints for admin purposes")
public class AdminController {

    private final AdminServiceImpl service;
    private final NoticiasServiceImpl noticiasService;
    private final GuildServiceImpl guildService;
    private final PurchaseServiceImpl purchaseService;
    private final ServerCustomerServiceImpl serverCustomerService;
    private final ShopServiceImpl shopService;
    private final Gson gson;


    @PostMapping("/authenticate")
    public ResponseEntity<AdminAuthenticationResponseDTO> authenticate(@RequestBody AdminAuthenticationRequestDTO request) throws CustomerServerNaoEncontradoException {
        AdminAuthenticationResponseDTO auth = service.authenticate(request);
        return ResponseEntity.ok(auth);
    }

    /* INICIO ENDPOINTS NOTICIAS*/

    @PatchMapping("/noticia/{idNoticia}")
    public ResponseEntity<Noticia> updateNoticia(@PathVariable Long idNoticia, @PathVariable Long customerId, @RequestBody UpdateNewsRequestDTO updatedNewsDTO) {
        return ResponseEntity.ok(noticiasService.updateNoticia(idNoticia, customerId, updatedNewsDTO));
    }

    @PostMapping("/noticia")
    public ResponseEntity<Void> saveNoticiaByCustomerId(@PathVariable Long customerId, @RequestBody CreateNewsRequestDTO dto) throws ParseException {
        noticiasService.saveNoticiaByCustomerId(customerId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/noticia/{idNoticia}")
    public ResponseEntity<Void> deleteNoticiaByIdAndCustomerId(@PathVariable Long idNoticia, @PathVariable Long customerId) {
        noticiasService.deleteNoticiaByIdAndCustomerId(idNoticia, customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/noticias")
    public ResponseEntity<DynamicTableResponseDTO> getNoticias(@PathVariable Long customerId) {
        return ResponseEntity.ok(noticiasService.getDynamicNoticiasTableByCustomerId(customerId));
    }

    /* FIM ENDPOINTS NOTICIAS*/

    /* ENDPOINTS GUILDMARKS*/


    @PatchMapping("/guildmark/{guildId}")
    public ResponseEntity<Void> updateGuildByCustomerId(@PathVariable Long customerId, @RequestBody AdminUpdateGuildmarkRequestDTO dto, @PathVariable String guildId) {
        guildService.updateByGuildIdAndByCustomerId(customerId, dto, guildId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping(path = "/guildmark")
    public ResponseEntity<Void> saveGuildByCustomerId(@PathVariable Long customerId, @RequestBody AdminSaveGuildmarkRequestDTO dto) {
        guildService.saveByCustomerId(customerId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/guildmark/{guildId}")
    public ResponseEntity<Void> deleteGuildByGuildId(@PathVariable Long customerId, @PathVariable String guildId) {
        guildService.deleteByIdAndCustomerId(customerId, guildId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/guildmarks")
    public ResponseEntity<DynamicTableResponseDTO> getGuildmarks(@PathVariable Long customerId) {
        return ResponseEntity.ok(guildService.getDynamicGuildmarkTableByCustomerId(customerId));
    }
    /* FIM ENDPOINTS GUILDMARKS*/




    /* ENDPOINTS COMPRAS*/
    @PatchMapping("/purchase/{preferenceId}")
    public ResponseEntity<Void> updatePurchaseByCustomerId(@PathVariable Long customerId, @PathVariable Long preferenceId, @RequestBody UpdatePurchaseRequestDTO dto) {
        purchaseService.updateByIdAndCustomerId(customerId, preferenceId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/purchase/{preferenceId}")
    public ResponseEntity<Void> deleteCPurchaseByCustomerId(@PathVariable Long customerId, @PathVariable Long preferenceId) {
        purchaseService.deletebyIdAndCustomerId(customerId, preferenceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/purchases")
    public ResponseEntity<DynamicTableResponseDTO> getPurchases(@PathVariable Long customerId) {
        DynamicTableResponseDTO tableData = purchaseService.getDynamicTableByCustomerId(customerId);
        return ResponseEntity.ok(tableData);
    }
    /* FIM DOS ENDPOINTS COMPRAS*/




    /* INICIO ENDPOINTS CONFIGURACOES DA CONTA*/
    @GetMapping("/conta")
    public ResponseEntity<ServerCustomer> getConta(@PathVariable Long customerId) {
        return ResponseEntity.ok(serverCustomerService.findById(customerId));
    }

    @PatchMapping("/conta")
    public ResponseEntity<Void> updateContaById(@PathVariable Long customerId, @RequestBody AdminMinhaContaUpdateDTO dto) {
        serverCustomerService.updateById(customerId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /* FIM ENDPOINTS CONFIGURACOES DA CONTA*/


    /* ENDPOINTS SHOP */


    @PatchMapping("/shop/{shopId}")
    public ResponseEntity<Void> updateItemShopByIdAndCustomerId(@PathVariable Long customerId, @RequestBody AdminUpdateShopRequestDTO dto, @PathVariable Long shopId) {
        shopService.updateItemShopByIdAndCustomerId(customerId, shopId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping(path = "/shop")
    public ResponseEntity<Void> saveItemShopByCustomerId(@PathVariable Long customerId, @RequestBody AdminSaveShopRequestDTO dto) {
        shopService.saveItemShopByCustomerId(customerId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/shop/{shopId}")
    public ResponseEntity<Void> deleteItemShopByShopId(@PathVariable Long customerId, @PathVariable Long shopId) {
        shopService.deleteItemShopByIdAndCustomerId(customerId, shopId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/shops")
    public ResponseEntity<DynamicTableResponseDTO> getItemShops(@PathVariable Long customerId) {
        return ResponseEntity.ok(shopService.getDynamicShopTableByCustomerId(customerId));
    }
    /* FIM ENDPOINTS SHOP*/

}
