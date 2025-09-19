package com.furryhub.petservices.controller;

import com.furryhub.petservices.model.entity.Pet;
import com.furryhub.petservices.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) { this.petService = petService; }

    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        Pet saved = petService.save(pet);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Pet>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(petService.findByOwner(ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.ok().build();
    }
}
