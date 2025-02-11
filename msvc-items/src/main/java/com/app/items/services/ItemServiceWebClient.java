package com.app.items.services;

import com.app.items.models.Item;
import com.app.items.models.Product;
import com.app.items.utils.GeneratorRandomNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceWebClient implements ItemService {

    private final WebClient.Builder client;

    @Override
    public List<Item> getItems() {
        return client
                .build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> new Item(product, GeneratorRandomNumber.getRandomNumber()))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> getItem(Long id) {
        Map<String, Long> params = Map.of("id", id);
        return client
                .build()
                .get()
                .uri("/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .bodyToMono(Product.class)
                .map(product -> new Item(product, GeneratorRandomNumber.getRandomNumber()))
//                .onErrorResume(WebClientResponseException.class,
//                        ex -> ex.getStatusCode().value() == 404 ? Mono.empty() : Mono.error(ex))
                .blockOptional();
    }
}
