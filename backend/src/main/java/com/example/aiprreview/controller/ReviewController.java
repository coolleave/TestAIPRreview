package com.example.aiprreview.controller;

import com.example.aiprreview.dto.ReviewResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "ok",
                "service", "aipr-review-backend",
                "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/sample")
    public ReviewResult sample() {
        return new ReviewResult(
                "PR-TEST-001",
                "ready",
                List.of(
                        "检测到 1 个可优化命名",
                        "测试分支合并流程可继续执行"
                )
        );
    }
}
