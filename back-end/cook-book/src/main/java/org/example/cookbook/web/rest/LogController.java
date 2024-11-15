package org.example.cookbook.web.rest;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.log.LoginLogDto;
import org.example.cookbook.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping("/login/{email}")
    public ResponseEntity<LoginLogDto[]> getLoginLogsByUserEmail(@PathVariable String email) {
        LoginLogDto[] loginLogByUserEmail = this.logService.getLoginLogByUserEmail(email);

        if (loginLogByUserEmail.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(loginLogByUserEmail);
    }

    @GetMapping("/login")
    public ResponseEntity<List<LoginLogDto>> getAllLoginLogs() {
        List<LoginLogDto> loginLogs = this.logService.getAllLoginLogs();

        if (loginLogs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(loginLogs);
    }
}
