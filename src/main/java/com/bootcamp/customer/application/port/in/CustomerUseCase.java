package com.bootcamp.customer.application.port.in;


import com.bootcamp.customer.domain.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerUseCase {
    Mono<Customer> findByDocNumber(String docNumber);
    Mono<Customer> create(Customer customer);
    Flux<Customer> findAll();
    Mono<Customer> update(String id, Customer customer);
    Mono<Void> delete (String id);

}
