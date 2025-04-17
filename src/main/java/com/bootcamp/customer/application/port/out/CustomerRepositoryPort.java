package com.bootcamp.customer.application.port.out;

import com.bootcamp.customer.domain.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepositoryPort {

    Mono<Customer> findByDocNumber(String docNumber);
    Mono<Customer> create(Customer customer);
    Flux<Customer> findAll();
    Mono<Customer> update(Customer customer);
    Mono<Customer> delete (String id);
    Mono<Boolean> existsByDocNumber(String docNumber);
}
