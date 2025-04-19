package com.bootcamp.customer.infrastructure.adapter.in.expose;

import com.bootcamp.customer.application.port.in.CustomerUseCase;
import com.bootcamp.customer.infrastructure.adapter.in.mapper.CustomerDtoMapper;
import com.bootcamp.customer.rest.api.CustomersApi;
import com.bootcamp.customer.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController implements CustomersApi{

    private final CustomerUseCase usecase;

    @Override
    public Mono<ResponseEntity<CustomerDto>> create(Mono<CustomerCreate> customerCreate, ServerWebExchange exchange) {
        return customerCreate
            .flatMap(create -> {
                return usecase.create(CustomerDtoMapper.toModel(create));
            })
            .map(model -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(CustomerDtoMapper.toDto(model)));
    }

   @Override
    public Mono<ResponseEntity<Void>> delete(String docNumber, ServerWebExchange exchange) {
        return usecase.delete(docNumber)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    public Mono<ResponseEntity<Flux<CustomerDto>>> findAll(ServerWebExchange exchange) {
        return Mono.just(
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(usecase.findAll().map(CustomerDtoMapper::toDto))
        );

    }


    @Override
    public Mono<ResponseEntity<CustomerDto>> findByDocument(String docNumber, ServerWebExchange exchange) {
        return usecase.findByDocNumber(docNumber)
            .map(res -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(CustomerDtoMapper.toDto(res)));
    }

    @Override
    public Mono<ResponseEntity<CustomerDto>> update(String docNumber, Mono<CustomerUpdate> customerUpdate, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<ResponseEntity<ValidateByDoc200Response>> validateByDoc(Mono<ValidateByDocRequest> validateByDocRequest, ServerWebExchange exchange) {
        return null;
    }
}
