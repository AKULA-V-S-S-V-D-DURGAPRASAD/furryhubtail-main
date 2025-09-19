package com.furryhub.petservices.service;

import com.furryhub.petservices.model.entity.Cart;
import com.furryhub.petservices.model.entity.CartItem;
import com.furryhub.petservices.model.entity.ServiceItem;
import com.furryhub.petservices.model.entity.User;
import com.furryhub.petservices.repository.CartItemRepository;
import com.furryhub.petservices.repository.CartRepository;
import com.furryhub.petservices.repository.ServiceItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final ServiceItemRepository serviceRepo;

    public CartService(CartRepository cartRepo, CartItemRepository itemRepo, ServiceItemRepository serviceRepo) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.serviceRepo = serviceRepo;
    }

    public Cart getOrCreateBySession(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            Cart c = new Cart();
            c.setSessionId(UUID.randomUUID().toString());
            return cartRepo.save(c);
        }
        return cartRepo.findBySessionId(sessionId)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setSessionId(sessionId);
                    return cartRepo.save(c);
                });
    }

    public Cart getOrCreateByUserId(Long userId) {
        return cartRepo.findByUser_Id(userId).orElseGet(() -> {
            Cart c = new Cart();
            User u = new User();
            u.setId(userId);
            c.setUser(u);
            return cartRepo.save(c);
        });
    }

    @Transactional
    public Cart addItemToCart(Cart cart, Long serviceItemId, int qty) {
        ServiceItem svc = serviceRepo.findById(serviceItemId)
                .orElseThrow(() -> new IllegalArgumentException("ServiceItem not found: " + serviceItemId));
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setServiceItem(svc);
        item.setQty(qty);
        item.setPrice(svc.getPrice());
        itemRepo.save(item);

        cart.getItems().add(item);
        return cartRepo.save(cart);
    }

    @Transactional
    public void mergeSessionToUserCart(String sessionId, Long userId) {
        Optional<Cart> sessionCartOpt = cartRepo.findBySessionId(sessionId);
        if (sessionCartOpt.isEmpty()) return;
        Cart sessionCart = sessionCartOpt.get();
        Cart userCart = getOrCreateByUserId(userId);

        for (CartItem it : sessionCart.getItems()) {
            it.setCart(userCart);
            itemRepo.save(it);
            userCart.getItems().add(it);
        }
        cartRepo.delete(sessionCart);
        cartRepo.save(userCart);
    }
}
