package com.billflow.backend.controller;

import com.billflow.backend.domain.Partner;
import com.billflow.backend.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.billflow.backend.domain.User;
import com.billflow.backend.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping({"/api/partners", "/api/customers"})
public class PartnerController {

    private final PartnerService partnerService;
    private final AuthService authService;

    public PartnerController(PartnerService partnerService, AuthService authService) {
        this.partnerService = partnerService;
        this.authService = authService;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return authService.getUserByUsername(auth.getName());
    }

    @GetMapping("/")
    public ResponseEntity<List<Partner>> getAllPartners() {
        return ResponseEntity.ok(partnerService.getAllPartners(getCurrentUser()));
    }

    @PostMapping("/")
    public ResponseEntity<Partner> createPartner(@RequestBody PartnerRequest request) {
        Partner partner = partnerService.createPartner(
            request.name(), 
            request.location(), 
            request.phoneNumber(), 
            getCurrentUser()
        );
        return ResponseEntity.ok(partner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePartner(@PathVariable String id) {
        try {
            partnerService.deletePartner(id);
            return ResponseEntity.ok("Partner deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DTOs
    public record PartnerRequest(String name, String location, String phoneNumber) {}
}
