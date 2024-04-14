package com.gamecms.backend.wyd.Controllers;

import com.gamecms.backend.wyd.Models.Blacklist;
import com.gamecms.backend.wyd.Models.Whitelist;
import com.gamecms.backend.wyd.Services.BlacklistServiceImpl;
import com.gamecms.backend.wyd.Services.WhiteListServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shareddata")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Shared Data", description = "Endpoints for Serving public Shared Data.")
public class SharedDataController {
    private final BlacklistServiceImpl blacklistService;
    private final WhiteListServiceImpl whitelistService;

    @GetMapping(path = "/blacklist", produces = "APPLICATION/JSON")
    public ResponseEntity<List<Blacklist>> getBlackList() {
        return new ResponseEntity<>(blacklistService.getBlackList(), HttpStatus.OK);
    }

    @GetMapping(path = "/whitelist", produces = "APPLICATION/JSON")
    public ResponseEntity<List<Whitelist>> getWhiteList() {
        return new ResponseEntity<>(whitelistService.getWhiteList(), HttpStatus.OK);
    }

    @GetMapping(path = "/getMyIp", produces = "APPLICATION/JSON")
    public ResponseEntity<Map<String, String>> getMyIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return new ResponseEntity<>(Map.of("ip", ipAddress), HttpStatus.OK);
    }


}
