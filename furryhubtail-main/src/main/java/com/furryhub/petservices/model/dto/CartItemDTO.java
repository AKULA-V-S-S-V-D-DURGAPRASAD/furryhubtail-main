package com.furryhub.petservices.model.dto;

public class CartItemDTO {
    private Long serviceItemId;
    private int quantity;

    public Long getServiceItemId() { return serviceItemId; }
    public void setServiceItemId(Long serviceItemId) { this.serviceItemId = serviceItemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
