package com.gamecms.integracao.wyd.Controllers;


import com.gamecms.integracao.wyd.DTO.ChangePassRequestDTO;
import com.gamecms.integracao.wyd.DTO.CreateAccountRequestDTO;
import com.gamecms.integracao.wyd.DTO.LoginAcountRequestDTO;
import com.gamecms.integracao.wyd.Services.AccountServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(
        name= "Account",
        description = "Operações de conta do jogador"
)
public class AccountController {

    private final AccountServiceImpl service;

    @PostMapping(path= "/create")
    public ResponseEntity<CreateAccountRequestDTO> create(@RequestBody  @Validated CreateAccountRequestDTO dtoRecebido) {
        return new ResponseEntity<>(service.create(dtoRecebido), HttpStatus.CREATED);
    }

    @PostMapping(path= "/login")
    public ResponseEntity<LoginAcountRequestDTO> login(@RequestBody @Validated LoginAcountRequestDTO dtoRecebido) {
        return ResponseEntity.ok(service.login(dtoRecebido));
    }

    @PatchMapping(path= "/changePass")
    public ResponseEntity<ChangePassRequestDTO> changePass(@RequestBody @Validated ChangePassRequestDTO dtoRecebido) {
        return ResponseEntity.ok(service.changePass(dtoRecebido));
    }

}
