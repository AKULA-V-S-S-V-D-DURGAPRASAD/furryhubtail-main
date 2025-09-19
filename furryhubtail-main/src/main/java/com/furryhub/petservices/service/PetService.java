package com.furryhub.petservices.service;

import com.furryhub.petservices.model.entity.Pet;
import com.furryhub.petservices.repository.PetRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PetService {
    private final PetRepository repo;

    public PetService(PetRepository repo) { this.repo = repo; }

    public Pet save(Pet p) { return repo.save(p); }

    public List<Pet> findByOwner(Long ownerId) { return repo.findByOwner_Id(ownerId); }

    public void delete(Long id) { repo.deleteById(id); }
}
