package com.app.items.services;

import com.app.items.models.Item;
import com.app.items.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceWebClientTest {

    @Test
    void get_items_returns_list_successfully() {
        // Arrange
        WebClient.Builder mockBuilder = mock(WebClient.Builder.class);
        WebClient mockClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec mockHeadersSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec mockRequestSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);

        List<Item> expectedItems = List.of(
                Item.builder().product(new Product()).quantity(1).build(),
                Item.builder().product(new Product()).quantity(2).build()
        );

        when(mockBuilder.build()).thenReturn(mockClient);
        when(mockClient.get()).thenReturn(mockHeadersSpec);
        when(mockHeadersSpec.uri("http://msvc-products/api/v1/products")).thenReturn(mockRequestSpec);
        when(mockRequestSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(mockRequestSpec);
        when(mockRequestSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToFlux(Item.class)).thenReturn(Flux.fromIterable(expectedItems));

        ItemServiceWebClient service = new ItemServiceWebClient(mockBuilder);

        // Act
        List<Item> result = service.getItems();

        // Assert
        assertThat(result).isEqualTo(expectedItems);
    }

    // Handle empty response when no items exist
    @Test
    void get_items_returns_empty_list_when_no_items() {
        // Arrange
        WebClient.Builder mockBuilder = mock(WebClient.Builder.class);
        WebClient mockClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec mockHeadersSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec mockRequestSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);

        when(mockBuilder.build()).thenReturn(mockClient);
        when(mockClient.get()).thenReturn(mockHeadersSpec);
        when(mockHeadersSpec.uri("http://msvc-products/api/v1/products")).thenReturn(mockRequestSpec);
        when(mockRequestSpec.accept(MediaType.APPLICATION_JSON)).thenReturn(mockRequestSpec);
        when(mockRequestSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToFlux(Item.class)).thenReturn(Flux.empty());

        ItemServiceWebClient service = new ItemServiceWebClient(mockBuilder);

        // Act
        List<Item> result = service.getItems();

        // Assert
        assertThat(result.isEmpty()).isTrue();
    }
}