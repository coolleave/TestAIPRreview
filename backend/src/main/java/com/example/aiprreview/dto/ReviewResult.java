package com.example.aiprreview.dto;

import java.util.List;

public record ReviewResult(
        String pullRequestId,
        String status,
        List<String> findings
) {
}
