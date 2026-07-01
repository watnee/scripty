package com.scripty.controller;

import com.scripty.service.DatabaseBackupService;
import com.scripty.service.DatabaseBackupService.BackupResult;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backup")
@ConditionalOnBean(DatabaseBackupService.class)
public class BackupController {

    private final DatabaseBackupService backupService;

    public BackupController(DatabaseBackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> triggerBackup() {
        BackupResult result = backupService.runBackup();
        Map<String, Object> response = Map.of(
            "success", result.success(),
            "totalRows", result.totalRows(),
            "durationMs", result.durationMs(),
            "error", result.error() != null ? result.error() : ""
        );
        if (result.success()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.internalServerError().body(response);
    }
}
