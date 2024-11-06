package org.example.cookbook.web.rest;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.log.LoginLogDto;
import org.example.cookbook.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping("/login")
    public ResponseEntity<LoginLogDto[]> getLoginLogsByUserEmail(@RequestBody String email) {
        LoginLogDto[] loginLogByUserEmail = this.logService.getLoginLogByUserEmail(email);

        if (loginLogByUserEmail == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(loginLogByUserEmail);
    }

    @GetMapping("/login/all")
    public ResponseEntity<List<LoginLogDto>> getAllLoginLogs() {
        List<LoginLogDto> loginLogs = this.logService.getAllLoginLogs();

        if (loginLogs == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(loginLogs);
    }
}
