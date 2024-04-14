package com.gamecms.integracao.wyd.Controllers;

import com.gamecms.integracao.wyd.DTO.ArquivoUpdateResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
@RestController
@RequestMapping("/updater/customer/{customerId}")
@Slf4j
@Tag(name="Updater Controller", description = "Updater methods")
public class UpdaterController {

    private final  String projectBase = UpdaterController.class.getProtectionDomain().getCodeSource().getLocation().getPath()
            .replace("file:/", "")
            .replace("wyd-1.jar!/BOOT-INF/classes!/", "")
            .trim();


    @GetMapping(value = "/getfiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ArquivoUpdateResponseDTO> getFiles(@PathVariable String customerId){

        String localDir = projectBase + "ClientAtualizado";

        log.info("UpdaterController - GetFiles localdir: " + localDir);
        try (Stream<Path> paths = Files.walk(Paths.get(localDir))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(n -> !n.toFile().getName().contains("UPD"))
                    .map(file -> new ArquivoUpdateResponseDTO(
                            file.toFile().getName(),
                            file.toFile().getPath().replace(localDir, ""),
                            file.toFile().lastModified(),
                            file.toFile().length()
                    ))
                    .toList();
        }
        catch (Exception e){
            log.error("UpdaterController - GetFiles localdir: " + e.getMessage());
            return null;
        }
    }

    @GetMapping(value = "/getfile/{pathNameEncoded}")
    public ResponseEntity<byte[]> getFile(@PathVariable String pathNameEncoded){
        String pathName = new String(Base64.getDecoder().decode(pathNameEncoded));


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=default.zip");

        String localDir = projectBase + "ClientAtualizado";

        log.info("UpdaterController - GetFile localdir: " + localDir + " File: " + pathName);

        try {
            File arquivo = Files.walk(Paths.get(localDir))
                    .filter(Files::isRegularFile)
                    .filter(n -> !n.toFile().getName().contains("UPD"))
                    .filter(n -> n.toFile().getPath().contains(pathName))
                    .map(n -> n.toFile())
                    .findFirst().get();

            byte[] dados = Files.readAllBytes(Path.of(arquivo.getPath()));
            return new ResponseEntity<>(dados, responseHeaders, HttpStatus.OK);
        }
        catch (Exception e){
            return null;
        }
    }
}
