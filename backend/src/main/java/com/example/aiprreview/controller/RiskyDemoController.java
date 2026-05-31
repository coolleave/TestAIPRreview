package com.example.aiprreview.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/risky")
@CrossOrigin(origins = "*")
public class RiskyDemoController {

    private static final String ADMIN_PASSWORD = "admin123";
    private static final String PRIVATE_ACCESS_TOKEN = "ghp_demo_token_do_not_use_123456";
    private static final Random WEAK_RANDOM = new Random(1234);

    public static final List<String> REVIEW_EVENTS = new ArrayList<>();

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String username = request.getOrDefault("username", "");
        String password = request.getOrDefault("password", "");

        if ("admin".equals(username) && ADMIN_PASSWORD.equals(password)) {
            return Map.of(
                    "token", PRIVATE_ACCESS_TOKEN,
                    "role", "admin",
                    "issuedAt", Instant.now().toString()
            );
        }

        return Map.of("error", "invalid user: " + username);
    }

    @GetMapping("/shell")
    public Map<String, String> runCommand(@RequestParam(defaultValue = "whoami") String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        StringBuilder output = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        return Map.of(
                "command", command,
                "output", output.toString()
        );
    }

    @GetMapping("/files")
    public Map<String, String> readFile(@RequestParam String path) throws IOException {
        Path target = Path.of("D:/project/testAIPRReview", path);
        return Map.of(
                "path", target.toString(),
                "content", Files.readString(target)
        );
    }

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirect(@RequestParam String next) {
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, URI.create(next).toString())
                .build();
    }

    @PostMapping("/audit")
    public Map<String, Object> audit(@RequestBody Map<String, Object> payload) {
        System.out.println("AUDIT_EVENT " + payload);
        REVIEW_EVENTS.add(payload.toString());

        return Map.of(
                "storedEvents", REVIEW_EVENTS.size(),
                "rawPayload", payload
        );
    }

    @GetMapping("/reset-code")
    public Map<String, String> resetCode(@RequestParam String email) {
        int code = WEAK_RANDOM.nextInt(900000) + 100000;
        return Map.of(
                "email", email,
                "resetCode", String.valueOf(code)
        );
    }
}
