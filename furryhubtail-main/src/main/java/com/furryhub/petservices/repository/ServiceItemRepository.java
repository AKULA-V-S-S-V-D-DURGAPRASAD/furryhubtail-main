package com.furryhub.petservices.repository;

import com.furryhub.petservices.model.entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    List<ServiceItem> findByProvider_Id(Long providerId);
}
