package com.gamecms.backend.wyd.Controllers;



import com.gamecms.backend.wyd.Models.Noticia;
import com.gamecms.backend.wyd.Models.NewsType;
import com.gamecms.backend.wyd.Services.NoticiasServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/customer/{customerId}/noticias")
@CrossOrigin()
@RequiredArgsConstructor
@Tag(name = "Noticias Controller", description = "Endpoints for Retrieve News")
public class NoticiasController {

    private final NoticiasServiceImpl service;

    @GetMapping()
    public ResponseEntity<List<Noticia>> findAllNoticias(@PathVariable Long customerId) {
        return new ResponseEntity<>(service.findAllNoticias(customerId), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Noticia> findNoticiasById(@PathVariable Long id, @PathVariable Long customerId) {
        return new ResponseEntity<>(service.findNoticiaById(id, customerId), HttpStatus.OK);
    }

    @GetMapping(path = "/tipo/{tipo}")
    public Page<Noticia> findByAllTipo(@PathVariable NewsType tipo, @RequestParam int page, @RequestParam int pageSize, @PathVariable Long customerId) {
        return service.findAllByTipo(tipo, PageRequest.of(page, pageSize), customerId);
    }


}
