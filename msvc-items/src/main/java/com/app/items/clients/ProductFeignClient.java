package com.app.items.clients;

import com.app.items.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "${app.feign.products.name}",
        path = "/api/v1/products"

//        url = "${app.feign.products.url}"
)
public interface ProductFeignClient {

    @GetMapping
    List<Product> getProducts();

    @GetMapping("/{id}")
    Product getProduct(@PathVariable Long id);
}
