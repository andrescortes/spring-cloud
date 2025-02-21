package com.app.items.controllers;

import com.app.items.models.Item;
import com.app.items.models.Product;
import com.app.items.services.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RefreshScope
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    @Qualifier("itemServiceWebClient")
    private final ItemService itemService;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final Environment environment;

    @Value("${conf.text}")
    private String text;

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems(@RequestParam(value = "name", required = false) String name, @RequestHeader(value = "Authorization", required = false) String authorization) {
        log.info("Name: {}, Authorization: {}", name, authorization);
        List<Item> items = itemService.getItems();
        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @CircuitBreaker(name = "getItemById", fallbackMethod = "fallbackGetItemById")
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService
                .getItem(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @TimeLimiter(name = "getItemById")
    @GetMapping("/details-timeout/{id}")
    public CompletableFuture<ResponseEntity<Item>> getItemById2(@PathVariable Long id) {
        return CompletableFuture
                .supplyAsync(() -> itemService
                        .getItem(id)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity
                                .notFound()
                                .build()
                        )
                );
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Item> getItemDetailsById(@PathVariable Long id) {
        return circuitBreakerFactory
                .create("getItemById")
                .run(() -> itemService
                                .getItem(id)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build()),
                        e -> {
                            log.error("Something went wrong: {}", e.getMessage());
                            Product product = Product
                                    .builder()
                                    .id(1L)
                                    .name("Default Product")
                                    .price(100.0)
                                    .createAt(LocalDate.now())
                                    .build();
                            return ResponseEntity.ok(new Item(product, 5));
                        }
                );
    }

    @GetMapping("/fetch-configs")
    public ResponseEntity<Map<String, String>> getConfig(@Value("${server.port}") String port) {
        Map<String, String> json = new HashMap<>();
        json.put("text", text);
        json.put("port", port);
        json.put("name", environment.getProperty("conf.author.name", String.class, "John Doe"));
        json.put("email", environment.getProperty("conf.author.email", String.class, "john@doe.com"));
        if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("dev")) {
            json.put("env", "dev");
        }
        if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("pdn")) {
            json.put("env", "pdn");
        }
        log.info("Config fetched: {}", json);
        return ResponseEntity.ok(json);
    }

    private ResponseEntity<Item> fallbackGetItemById(Throwable e) {
        log.error("Fallback method called for getItemById and exception: {}", e.getMessage());
        Product product = Product
                .builder()
                .id(1L)
                .name("Default Product")
                .price(100.0)
                .createAt(LocalDate.now())
                .build();
        return ResponseEntity.ok(new Item(product, 5));
    }
}
