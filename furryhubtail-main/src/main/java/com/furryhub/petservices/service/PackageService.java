
package com.furryhub.petservices.service;


import com.furryhub.petservices.model.dto.PackageRequestDTO;

import com.furryhub.petservices.model.entity.Package;
import com.furryhub.petservices.model.entity.Provider;
import com.furryhub.petservices.repository.PackageRepository;
import com.furryhub.petservices.repository.ProviderRepository;
import com.furryhub.petservices.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class PackageService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PackageRepository packageRepository;

    
    @Transactional
    public PackageRequestDTO addPackage(String providerEmail, String name, String description, 
                              java.math.BigDecimal price, Duration duration) {
        
        Provider provider = providerRepository.findByUser_Email(providerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with email: " + providerEmail));

        
        Package servicePackage = Package.builder()
                .provider(provider)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .available(true) 
                .build();

        packageRepository.save(servicePackage);
        System.out.println("Pacakge addded");
		return convertToDTO(servicePackage);
        
    }
     
    private PackageRequestDTO convertToDTO(Package booking) {
        return PackageRequestDTO.builder()
                .id(booking.getId())
                .providerId(booking.getProvider().getId())
                .price(booking.getPrice())
                .description(booking.getDescription())
                .duration(booking.getDuration())
                .name(booking.getName())
                .build();
    }
   
    @Transactional(readOnly = true)
    public Iterable<Package> getAllPackages() {
        return packageRepository.findAll();
    }
}