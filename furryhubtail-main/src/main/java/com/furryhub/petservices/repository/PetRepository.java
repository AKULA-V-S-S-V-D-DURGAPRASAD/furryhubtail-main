package com.furryhub.petservices.repository;

import com.furryhub.petservices.model.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwner_Id(Long ownerId);
}
