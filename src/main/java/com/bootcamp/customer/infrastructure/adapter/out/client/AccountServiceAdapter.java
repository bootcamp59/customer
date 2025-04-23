package com.bootcamp.customer.infrastructure.adapter.out.client;

import com.bootcamp.customer.application.port.out.AccountServicePort;
import com.bootcamp.customer.domain.dto.AccountDto;
import com.bootcamp.customer.infrastructure.config.CustomerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountServiceAdapter  implements AccountServicePort {

    private final WebClient.Builder webClientBuilder;
    private final CustomerProperties properties;


    @Override
    public Mono<List<AccountDto>> getAccountByDocument(String documentNumber) {
        var url = properties.getMsAccountApi() + "/" + documentNumber;

        return webClientBuilder.build()
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
}
