package com.gamecms.integracao.wyd.Controllers;


import com.gamecms.integracao.wyd.DTO.ChangePassRequestDTO;
import com.gamecms.integracao.wyd.DTO.CreateAccountRequestDTO;
import com.gamecms.integracao.wyd.DTO.LoginAcountRequestDTO;
import com.gamecms.integracao.wyd.Models.Character;
import com.gamecms.integracao.wyd.Services.AccountServiceImpl;
import com.gamecms.integracao.wyd.Services.CharacterServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
@Tag(
        name= "Ranking",
        description = "Operações de ranking"
)
public class RankingController {

    private final CharacterServiceImpl service;

    @GetMapping(produces = "APPLICATION/JSON")
    public ResponseEntity<String> getRanking() {
        return new ResponseEntity<>(service.findFirst100ByFrags(), HttpStatus.OK);
    }

    @GetMapping(path = "/miniranking", produces = "APPLICATION/JSON")
    public ResponseEntity<String> getMiniRanking() {
        return new ResponseEntity<>(service.findFirst5ByFrags(), HttpStatus.OK);
    }


}
