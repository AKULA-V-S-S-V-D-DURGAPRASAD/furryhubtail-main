package com.furryhub.petservices.controller;

import com.furryhub.petservices.model.dto.CartDTO;
import com.furryhub.petservices.model.entity.Cart;
import com.furryhub.petservices.service.CartService;
import com.furryhub.petservices.repository.CartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CartRepository cartRepo;

    public CartController(CartService cartService, CartRepository cartRepo) {
        this.cartService = cartService;
        this.cartRepo = cartRepo;
    }

    @PostMapping("/session")
    public ResponseEntity<?> createSessionCart(@RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        Cart c = cartService.getOrCreateBySession(sessionId);
        return ResponseEntity.ok(java.util.Map.of("sessionId", c.getSessionId(), "cartId", c.getId()));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartDTO dto,
                                       @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
                                       @RequestHeader(value = "Authorization", required = false) String auth) {
        Cart cart;
        // If user is authenticated, frontend should pass userId or JWT; here we fallback to session cart
        cart = cartService.getOrCreateBySession(sessionId);
        cart = cartService.addItemToCart(cart, dto.getServiceItemId(), dto.getQty());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/merge")
    public ResponseEntity<?> mergeCart(@RequestParam String sessionId, @RequestParam Long userId) {
        cartService.mergeSessionToUserCart(sessionId, userId);
        return ResponseEntity.ok(java.util.Map.of("merged", true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable Long id) {
        return cartRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
