package com.app.items.conf;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConf {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> breakerFactoryCustomizer() {
        return factory -> factory
                .configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(CircuitBreakerConfig
                                .custom()
                                .slidingWindowSize(10) // 10 requests in open state by default is 100
                                .failureRateThreshold(50) // 50% failures in open state
                                .waitDurationInOpenState(Duration.ofSeconds(10L)) // 10 seconds to wait before closing the circuit
                                .permittedNumberOfCallsInHalfOpenState(5) // 5 calls in half-open state
                                .slowCallDurationThreshold(Duration.ofSeconds(2L)) // 5 seconds to wait before calling the service again
                                .slowCallRateThreshold(50) // 50% failures in slow call rate
                                .build()
                        )
                        .timeLimiterConfig(TimeLimiterConfig
                                .custom()
                                .timeoutDuration(Duration.ofSeconds(3L)) // 1 second timeout
                                .build()
                        )
                        .build()
                );
    }
}
