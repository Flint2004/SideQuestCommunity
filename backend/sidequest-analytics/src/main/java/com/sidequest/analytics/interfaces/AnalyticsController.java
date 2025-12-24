package com.sidequest.analytics.interfaces;

import com.sidequest.analytics.application.AnalyticsService;
import com.sidequest.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> getStats() {
        return Result.success(analyticsService.getDashboardStats());
    }

    @GetMapping("/dashboard/top-posts")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<Map<String, Object>>> getTopPosts() {
        return Result.success(analyticsService.getTopPosts());
    }

    @GetMapping("/users/{id}/stats")
    public Result<Map<String, Object>> getUserStats(@PathVariable Long id) {
        // Anyone can see their own stats or public stats
        return Result.success(analyticsService.getUserStats(id));
    }
}

