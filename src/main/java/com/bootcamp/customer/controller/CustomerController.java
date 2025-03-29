package com.bootcamp.customer.controller;

import com.bootcamp.customer.business.CustomerService;
import com.bootcamp.customer.model.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public Mono<ResponseEntity<Customer>> create(@RequestBody Customer customer){
        return service.create(customer)
            .map(savedCustomer -> ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer))
            .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Customer>> findById(@PathVariable String id){
        return service.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Customer> findAll(){
        return service.findAll();
    }


}
