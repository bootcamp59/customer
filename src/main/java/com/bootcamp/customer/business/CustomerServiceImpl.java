package com.bootcamp.customer.business;

import com.bootcamp.customer.model.entity.Customer;
import com.bootcamp.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final WebClient.Builder webClientBuilder;

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
    public Mono<Map<String, Object>> findProductsById(String id) {
        var urlAccount = "http://localhost:8086/api/v1/account/customer/"+id;
        var urlCredit = "http://localhost:8087/api/v1/credit/customer/"+id;
        Mono<Object> cuentas = findProducts(urlAccount);
        Mono<Object> creditos = findProducts(urlCredit);
        return Mono.zip(cuentas, creditos)
                .map(tuple -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("accounts", tuple.getT1());
                    result.put("credits", tuple.getT2());
                    return result;
                });
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



    private Mono<Object> findProducts(String url){

        return webClientBuilder.build()
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorMap( e -> new RuntimeException("error al actualizar deposito"))
                .doOnError(o -> System.out.println("solo logging error"));

    }
}
