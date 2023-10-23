package com.urbanthreads.orderservice.DTO;

public class ItemDTO {
    public ItemDTO(Long id, int quantity) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    Long id;
     Integer quantity;
}
