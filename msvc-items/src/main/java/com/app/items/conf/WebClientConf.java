package com.app.items.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class WebClientConf {

    @Value("${app.load-balancer.products}")
    private String productsUrl;

    @Bean
    @LoadBalanced
    public WebClient.Builder webClient() {
        log.info("Load balancer product url: {}", productsUrl);
        return WebClient
                .builder()
                .baseUrl(productsUrl);
    }
}
