package com.bootcamp.customer.business;

import com.bootcamp.customer.model.entity.Customer;
import com.bootcamp.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public Mono<Customer> create(Customer customer) {
        return validateCustomer(customer)
            .flatMap(validatedCustomer -> {
                validatedCustomer.setCreatedAt(LocalDateTime.now());
                validatedCustomer.setUpdatedAt(LocalDateTime.now());
                return repository.save(validatedCustomer);
            });
    }

    @Override
    public Mono<Customer> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }

    @Override
    public Flux<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Customer> update(String id, Customer customer) {
        return repository.findById(id)
            .flatMap(existsCustomer -> {
                existsCustomer.setName(customer.getName());
                existsCustomer.setEmail(customer.getEmail());
                existsCustomer.setPhone(customer.getPhone());
                existsCustomer.setUpdatedAt(LocalDateTime.now());
                return repository.save(existsCustomer);
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }

    @Override
    public Mono<Customer> delete(String id) {
        return repository.findById(id)
            .flatMap(customer -> {
                if(!customer.getAccountIds().isEmpty()){
                    return Mono.error(new RuntimeException("Cannot delete customer with active accounts"));
                }
                customer.setStatus((byte) 0);
                return repository.save(customer);
            })
            .switchIfEmpty( Mono.error(new RuntimeException("customer not found")));
    }

    @Override
    public Mono<Customer> findByDocNumber(String docNumber) {
        return repository.findByDocNumber(docNumber)
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }


    private Mono<Customer> validateCustomer(Customer customer){
        return repository.existsByDocNumber(customer.getDocNumber())
            .flatMap(exists -> {
                if(exists){
                    return Mono.error(new RuntimeException("Document number already exists"));
                }
                if(customer.getType() == null){
                    return Mono.error(new RuntimeException("Customer type is required"));
                }
                if(customer.getDocNumber() == null || customer.getDocNumber().isEmpty()){
                    return Mono.error(new RuntimeException("Document number is required"));
                }

                return Mono.just(customer);
            });
    }
}
