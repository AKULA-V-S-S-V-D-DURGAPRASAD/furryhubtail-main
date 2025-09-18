
package com.furryhub.petservices.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Provider {
    @Id
    private Long id; // Primary Key and Foreign Key to User.id

    private String specialization;

    private Integer experience; // in years

    private String licenseNumber;

    private String petStoreName;

    private String petClinicLocation;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Manages the forward part of the relationship
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Package> packages = new ArrayList<>();

    @OneToOne
    @MapsId
    @JoinColumn(name = "id") // MapsId uses the same column as @Id
    private User user;
    
    private String fieldType;
    
    private String address;
    
    private String city;
    
    private String houseVisit;
    
    private String businessContactNumber;
    
    private String onlineService;
    
    
}