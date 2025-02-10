package com.app.gatewayserver.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    private static final String TOKEN_CUSTOM = "X-Token-Custom";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Executing this filter before proceeding to the next filter in the chain.");
        ServerHttpRequest serverHttpRequest = exchange
                .getRequest()
                .mutate()
                .headers(headers -> headers.add(TOKEN_CUSTOM, "CustomValue"))
                .build();

        ServerWebExchange webExchange = exchange
                .mutate()
                .request(serverHttpRequest)
                .build();

        return chain.filter(webExchange)
                .then(Mono.fromRunnable(() -> {
                    log.info("executing this runnable after the filter chain has been executed.");
                    String token = webExchange.getRequest().getHeaders().getFirst(TOKEN_CUSTOM);
                    log.info("Token received: {}", token);
                    if(Objects.nonNull(token)) {
                        webExchange.getResponse().getHeaders().add(TOKEN_CUSTOM, token);
                    }

                    webExchange
                            .getResponse()
                            .getCookies()
                            .add("color", ResponseCookie.from("color", "red").build());
                    webExchange
                            .getResponse()
                            .getHeaders()
                            .setContentType(MediaType.APPLICATION_JSON);
                }));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
