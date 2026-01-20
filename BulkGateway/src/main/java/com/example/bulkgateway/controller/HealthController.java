package com.example.bulkgateway.controller;
import com.example.bulkgateway.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/")
public class HealthController {
    @GetMapping
    public ResponseEntity<ApiResponse<String>> home() {
        return ResponseEntity.ok(ApiResponse.success("Welcome to BulkGateway API!"));
    }
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", LocalDateTime.now().toString());
        healthInfo.put("application", "BulkGateway API");
        healthInfo.put("version", "1.0.0");
        return ResponseEntity.ok(ApiResponse.success("Application is healthy", healthInfo));
    }
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> info() {
        Map<String, Object> appInfo = new HashMap<>();
        appInfo.put("name", "BulkGateway API");
        appInfo.put("version", "1.0.0");
        appInfo.put("description", "Bulk Gateway API for message management");
        appInfo.put("author", "Development Team");
        return ResponseEntity.ok(ApiResponse.success(appInfo));
    }
}
