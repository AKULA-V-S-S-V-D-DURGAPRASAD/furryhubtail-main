package com.furryhub.petservices.service;

import com.furryhub.petservices.model.entity.ServiceItem;
import com.furryhub.petservices.repository.ServiceItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceItemService {
    private final ServiceItemRepository repo;

    public ServiceItemService(ServiceItemRepository repo) { this.repo = repo; }

    public ServiceItem save(ServiceItem s) { return repo.save(s); }

    public List<ServiceItem> findAll() { return repo.findAll(); }

    public List<ServiceItem> findByProvider(Long providerId) { return repo.findByProvider_Id(providerId); }
}
