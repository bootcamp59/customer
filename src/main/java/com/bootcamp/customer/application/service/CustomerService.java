package com.bootcamp.customer.application.service;

import com.bootcamp.customer.application.port.in.CustomerUseCase;
import com.bootcamp.customer.application.port.out.CustomerRepositoryPort;
import com.bootcamp.customer.domain.dto.AccountDto;
import com.bootcamp.customer.domain.dto.ConsolidateProductoSummary;
import com.bootcamp.customer.domain.dto.CreditDto;
import com.bootcamp.customer.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService implements CustomerUseCase {

    private final CustomerRepositoryPort port;
    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Customer> findByDocNumber(String docNumber) {
        return port.findByDocNumber(docNumber)
            .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }

    @Override
    public Mono<Customer> create(Customer customer) {
       return validateCustomer(customer)
           .flatMap(newCustomer -> {
               newCustomer.setCreatedAt(LocalDateTime.now());
               newCustomer.setUpdatedAt(LocalDateTime.now());
               return port.create(newCustomer);
           });
    }

    @Override
    public Flux<Customer> findAll() {
        return port.findAll();
    }

    @Override
    public Mono<Customer> update(String id, Customer customer) {
        return port.findByDocNumber(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")))
            .flatMap(existing -> {
                mergeNonNullValues(existing, customer);
                existing.setUpdatedAt(LocalDateTime.now());
                return port.update(existing);
            });
    }

    @Override
    public Mono<Void> delete(String id) {
        return port.findByDocNumber(id)
            .flatMap(customer -> {
                if(!customer.getAccountIds().isEmpty()){
                    return Mono.error(new RuntimeException("Cannot delete customer with active accounts"));
                }
                customer.setStatus("INACTIVE");
                return port.update(customer).then();
            })
            .switchIfEmpty( Mono.error(new RuntimeException("customer not found")));
    }

    @Override
    public Mono<ConsolidateProductoSummary> productConsolidatedSummary(String document) {
        var accountResp = getAccount("http://localhost:8086/api/v1/account/"+ document);
        var creditResp = getCredit("http://localhost:8087/api/v1/credit/customer/"+ document);
        var clienteResp = port.findByDocNumber(document);

        return Mono.zip(accountResp, creditResp, clienteResp)
            .map(tuple3 -> {
                var accounts = tuple3.getT1();
                var credits = tuple3.getT2();
                var customer = tuple3.getT3();

                return ConsolidateProductoSummary.builder()
                    .name(customer.getName())
                    .document(customer.getDocNumber())
                    .accounts(accounts)
                    .credits(credits)
                    .build();
            });
    }

    private void mergeNonNullValues(Customer target, Customer source) {
        Optional.ofNullable(source.getName()).ifPresent(target::setName);
        Optional.ofNullable(source.getDocType()).ifPresent(target::setDocType);
        Optional.ofNullable(source.getDocNumber()).ifPresent(target::setDocNumber);
        Optional.ofNullable(source.getEmail()).ifPresent(target::setEmail);
        Optional.ofNullable(source.getPhone()).ifPresent(target::setPhone);
        Optional.ofNullable(source.getAddress()).ifPresent(target::setAddress);
        Optional.ofNullable(source.getType()).ifPresent(target::setType);
        Optional.ofNullable(source.getPerfil()).ifPresent(target::setPerfil);
    }

    private Mono<Customer> validateCustomer(Customer customer){
        return port.existsByDocNumber(customer.getDocNumber())
                .flatMap(exists -> {
                    if(exists){
                        log.error("Document number already exists");
                        return Mono.error(new RuntimeException("Document number already exists"));
                    }
                    if(customer.getType() == null){
                        log.error("Customer type is required");
                        return Mono.error(new RuntimeException("Customer type is required"));
                    }
                    if(customer.getDocNumber() == null || customer.getDocNumber().isEmpty()){
                        log.error("Document number is required");
                        return Mono.error(new RuntimeException("Document number is required"));
                    }

                    return Mono.just(customer);
                });
    }

    private Mono<List<AccountDto>> getAccount(String url){
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

    private Mono<List<CreditDto>> getCredit(String url){
        return webClientBuilder.build()
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
