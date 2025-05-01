package com.bootcamp.customer.infrastructure.adapter.out.client;

import com.bootcamp.customer.application.port.out.AccountServiceClientPort;
import com.bootcamp.customer.domain.dto.AccountDto;
import com.bootcamp.customer.infrastructure.config.CustomerProperties;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountServiceClientAdapter implements AccountServiceClientPort {

    private final WebClient webClient;
    private final CustomerProperties properties;

    @Override
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackAccounts")
    public Mono<List<AccountDto>> getAccountByDocument(String documentNumber) {
        var url = properties.getMsAccountApi() + "/" + documentNumber;

        return webClient
            .get()
            .uri(url)
            .retrieve()
            .bodyToFlux(AccountDto.class)
            .collectList()
            .doOnNext(f -> {
                log.info("conexion exitosa al serivicio: {}", url + f);
            })
            .doOnError( err ->  log.info("no se logro la conexion al serivicio: {}", url));
    }

    public Mono<List<AccountDto>> fallbackAccounts(String documentNumber, Throwable t) {
        log.warn("Fallback activado para accountService: {}", t.getMessage());
        return Mono.just(List.of());
    }
}
