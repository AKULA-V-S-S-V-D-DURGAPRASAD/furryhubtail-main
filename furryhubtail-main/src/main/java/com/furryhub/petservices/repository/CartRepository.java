package com.furryhub.petservices.repository;

import com.furryhub.petservices.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findBySessionId(String sessionId);
    Optional<Cart> findByUser_Id(Long userId);
}
