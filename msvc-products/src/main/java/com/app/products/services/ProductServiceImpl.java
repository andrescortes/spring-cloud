package com.app.products.services;

import com.app.products.entities.Product;
import com.app.products.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Environment environment;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return ((List<Product>) productRepository.findAll())
                .stream()
                .map(product -> {
                    product.setPort(getPort());
                    return product;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setPort(getPort());
                    return product;
                });
    }

    private int getPort() {
        return environment.getProperty("server.port",Integer.class, 9000);
    }
}
