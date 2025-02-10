package com.app.items.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Product {
    private Long id;
    private String name;
    private Double price;
    private LocalDate createAt;
    private int port;
}
