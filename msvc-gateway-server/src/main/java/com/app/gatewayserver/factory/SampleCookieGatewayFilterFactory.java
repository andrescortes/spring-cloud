package com.app.gatewayserver.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {

    public SampleCookieGatewayFilterFactory() {
        super(ConfigurationCookie.class);
    }

    @Override
    public GatewayFilter apply(ConfigurationCookie config) {
        log.info("Executing this filter before proceeding to the next filter in the chain with message: {}", config.getMessage());
        return new OrderedGatewayFilter((exchange, chain) -> chain
                .filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    Optional.ofNullable(config.getValue())
                            .ifPresent(cookieValue -> exchange.getResponse().addCookie(ResponseCookie.from(config.getName(), cookieValue).build()));
                    log.info("executing this runnable after the filter chain has been executed.");
                    log.info("Cookie added: {}", config.getName());
                })), 100);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("message", "name", "value");
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigurationCookie {
        private String name;
        private String value;
        private String message;
    }
}
