package com.gamecms.backend.wyd.Controllers;

import com.gamecms.backend.wyd.Models.ServerCustomer;
import com.gamecms.backend.wyd.Services.GuildServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;


@RestController()
@RequestMapping("/api/v1/customer/{customerId}/guild")
@RequiredArgsConstructor
@Tag(name = "Guild Controller", description = "Endpoints for Guild Management")
public class GuildController {

    private final GuildServiceImpl service;

    @GetMapping(path = "/guildmark/{id}", produces = "image/bmp")
    public ResponseEntity<Resource> findByIdAndCustomerId(@PathVariable("id") String id, @PathVariable Long customerId) {
        ByteArrayResource resource = service.findByIdAndCustomerId(id, customerId);

        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .header("Content-type", "image/bmp")
                .header("Content-disposition", "inline; filename=" + id)
                .body(resource);
    }



    @PostMapping(path = "/guildmark", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadGuildmark(
            @RequestParam("image") MultipartFile file,
            @RequestParam String guildId,
            @RequestParam String username,
            @RequestParam String captcha,
            @RequestParam String guildPassword,
            @PathVariable Long customerId) {
        service.saveByCustomerId(file, guildId, username, captcha, guildPassword, customerId);
        return ResponseEntity.ok(new Object[]{ guildId, username, captcha, customerId });
    }


}
