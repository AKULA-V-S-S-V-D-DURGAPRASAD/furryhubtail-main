package com.furryhub.petservices.model.dto;

public class CartDTO {
    private Long serviceItemId;
    private Integer qty = 1;

    public Long getServiceItemId() { return serviceItemId; }
    public void setServiceItemId(Long serviceItemId) { this.serviceItemId = serviceItemId; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
}
