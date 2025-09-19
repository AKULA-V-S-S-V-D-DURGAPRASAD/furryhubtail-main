package com.furryhub.petservices.controller;

import com.furryhub.petservices.model.entity.ServiceItem;
import com.furryhub.petservices.service.ServiceItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceItemController {
    private final ServiceItemService svc;

    public ServiceItemController(ServiceItemService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<List<ServiceItem>> all() { return ResponseEntity.ok(svc.findAll()); }

    @PostMapping
    public ResponseEntity<ServiceItem> create(@RequestBody ServiceItem s) { return ResponseEntity.ok(svc.save(s)); }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ServiceItem>> byProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(svc.findByProvider(providerId));
    }
}
