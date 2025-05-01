package com.bootcamp.customer.infrastructure.adapter.out.client;

import com.bootcamp.customer.application.port.out.CreditServiceClientPort;
import com.bootcamp.customer.domain.dto.CreditDto;
import com.bootcamp.customer.infrastructure.config.CustomerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditServiceClientAdapter implements CreditServiceClientPort {

    private final WebClient webClient;
    private final CustomerProperties properties;

    @Override
    public Mono<List<CreditDto>> getCreditByDocument(String documentNumber) {

        var url = properties.getMsCreditApi() + "/" + documentNumber;

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(CreditDto.class)
                .collectList()
                .doOnNext(f -> {
                    log.info("conexion exitosa al serivicio: {}", url + f);
                })
                .doOnError( err ->  log.info("no se logro la conexion al serivicio: {}", url));
    }
}
