package com.gamecms.backend.wyd.Controllers;


import com.gamecms.backend.wyd.Services.ImageServiceImpl;
import com.gamecms.backend.wyd.Services.ShopServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/customer/{customerId}/image")
@RequiredArgsConstructor
@Tag(name = "Image Management Controller", description = "Endpoints for Images")
public class ImageController {

    private final ImageServiceImpl service;




}
