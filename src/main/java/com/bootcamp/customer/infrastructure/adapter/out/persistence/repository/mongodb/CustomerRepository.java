package com.bootcamp.customer.infrastructure.adapter.out.persistence.repository.mongodb;

import com.bootcamp.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<CustomerEntity, String> {
    Mono<CustomerEntity> findByDocNumber(String docNumber);
    Mono<Boolean> existsByDocNumber(String docNumber);

}
