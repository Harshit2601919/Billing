package com.billflow.backend.controller;

import com.billflow.backend.domain.User;
import com.billflow.backend.service.AuthService;
import com.billflow.backend.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final PartnerService partnerService;
    private final AuthService authService;

    public StatsController(PartnerService partnerService, AuthService authService) {
        this.partnerService = partnerService;
        this.authService = authService;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return authService.getUserByUsername(auth.getName());
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomers() {
        return ResponseEntity.ok(partnerService.getTopPartners(getCurrentUser()));
    }

    @GetMapping("/revenue-analysis")
    public ResponseEntity<List<Map<String, Object>>> getRevenueAnalysis(@RequestParam(defaultValue = "monthly") String period) {
        return ResponseEntity.ok(partnerService.getRevenueAnalysis(getCurrentUser(), period));
    }
}
