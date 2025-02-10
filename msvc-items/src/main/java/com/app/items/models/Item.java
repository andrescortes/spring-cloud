package com.app.items.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Item {
    private Product product;
    private int quantity;

    public Double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
