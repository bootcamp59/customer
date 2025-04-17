package com.bootcamp.customer.application.service;

import com.bootcamp.customer.application.port.in.CustomerUseCase;
import com.bootcamp.customer.application.port.out.CustomerRepositoryPort;
import com.bootcamp.customer.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService implements CustomerUseCase {

    private final CustomerRepositoryPort port;

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
}
