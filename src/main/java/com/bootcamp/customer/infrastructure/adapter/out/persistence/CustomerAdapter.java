package com.bootcamp.customer.infrastructure.adapter.out.persistence;

import com.bootcamp.customer.application.port.out.CustomerRepositoryPort;
import com.bootcamp.customer.domain.model.Customer;
import com.bootcamp.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import com.bootcamp.customer.infrastructure.adapter.out.persistence.mapper.CustomerMapper;
import com.bootcamp.customer.infrastructure.adapter.out.persistence.repository.mongodb.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerAdapter implements CustomerRepositoryPort {

    private final CustomerRepository repository;

    @Override
    public Mono<Customer> findByDocNumber(String docNumber) {
        return repository.findByDocNumber(docNumber)
            .map(CustomerMapper::toModel);
    }

    @Override
    public Mono<Customer> create(Customer customer) {
        return repository.save(CustomerMapper.toEntity(customer))
            .map(CustomerMapper::toModel);
    }

    @Override
    public Flux<Customer> findAll() {
        return repository.findAll()
            .map(CustomerMapper::toModel);
    }

    @Override
    public Mono<Customer> update(Customer customer) {
        return repository.save(CustomerMapper.toEntity(customer))
            .map(CustomerMapper::toModel);
    }

    @Override
    public Mono<Customer> delete(String id) {
        return repository.deleteById(id)
            .thenReturn(Customer.builder().id(id).build());
    }

    @Override
    public Mono<Boolean> existsByDocNumber(String docNumber) {
        return repository.existsByDocNumber(docNumber);
    }


}
