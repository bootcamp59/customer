package com.bootcamp.customer.business;

import com.bootcamp.customer.model.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CustomerService {
   Mono<Customer> create(Customer customer);
   Mono<Customer> findById(String id);
   Mono<Map<String, Object>> findProductsById(String id);
   Flux<Customer> findAll();
   Mono<Customer> update(String id, Customer customer);
   Mono<Customer> delete (String id);
   Mono<Customer> findByDocNumber(String docNumber);
}
