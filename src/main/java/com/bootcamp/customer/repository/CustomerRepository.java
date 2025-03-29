package com.bootcamp.customer.repository;

import com.bootcamp.customer.model.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Mono<Customer> findByDocNumber(String docNumber);
    Mono<Boolean> existsByDocNumber(String docNumber);
    Mono<Long> countByType(Customer.CustomerType type);
}
