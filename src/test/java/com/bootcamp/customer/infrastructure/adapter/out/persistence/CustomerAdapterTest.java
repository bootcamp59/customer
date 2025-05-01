package com.bootcamp.customer.infrastructure.adapter.out.persistence;

import com.bootcamp.customer.domain.model.Customer;
import com.bootcamp.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import com.bootcamp.customer.infrastructure.adapter.out.persistence.repository.mongodb.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CustomerAdapterTest {

    @InjectMocks
    private  CustomerAdapter adapter;

    @Mock
    private CustomerRepository repository;

    @Test
    void testFindByDocument(){
        var customer = CustomerEntity.builder()
            .name("Roberth")
            .docType("dni")
            .docNumber("12345678")
            .type(Customer.CustomerType.PERSONAL)
            .perfil(Customer.PerfilType.NORMAL)
            .status("ACTIVO")
            .phone("999999999")
            .email("emeal@emeal.com")
            .build();

        Mockito.when(repository.findByDocNumber("12345678"))
                .thenReturn(Mono.just(customer));

        Mono<Customer> result = adapter.findByDocNumber("12345678");

        StepVerifier.create(result)
            .assertNext( item -> {
                Assertions.assertEquals("Roberth", item.getName());
                Assertions.assertEquals("dni", item.getDocType());
                Assertions.assertEquals("12345678", item.getDocNumber());
                Assertions.assertEquals(Customer.CustomerType.PERSONAL, item.getType());
                Assertions.assertEquals(Customer.PerfilType.NORMAL, item.getPerfil());
                Assertions.assertEquals("ACTIVO", item.getStatus());
                Assertions.assertEquals("999999999", item.getPhone());
                Assertions.assertEquals("emeal@emeal.com", item.getEmail());
            })
            .verifyComplete();
    }

    @Test
    void testCreateCustomer() {
        var customer = Customer.builder()
                .name("Roberth")
                .docType("dni")
                .docNumber("12345678")
                .type(Customer.CustomerType.PERSONAL)
                .perfil(Customer.PerfilType.NORMAL)
                .status("ACTIVO")
                .phone("999999999")
                .email("emeal@emeal.com")
                .build();

        var entity = CustomerEntity.builder()
                .name("Roberth")
                .docType("dni")
                .docNumber("12345678")
                .type(Customer.CustomerType.PERSONAL)
                .perfil(Customer.PerfilType.NORMAL)
                .status("ACTIVO")
                .phone("999999999")
                .email("emeal@emeal.com")
                .build();

        Mockito.when(repository.save(Mockito.any(CustomerEntity.class)))
                .thenReturn(Mono.just(entity));

        Mono<Customer> result = adapter.create(customer);

        StepVerifier.create(result)
                .assertNext(created -> {
                    Assertions.assertEquals("Roberth", created.getName());
                    Assertions.assertEquals("12345678", created.getDocNumber());
                })
                .verifyComplete();
    }

    @Test
    void testFindAllCustomers() {
        var entity1 = CustomerEntity.builder()
                .name("Alice")
                .docNumber("1111")
                .build();

        var entity2 = CustomerEntity.builder()
                .name("Bob")
                .docNumber("2222")
                .build();

        Mockito.when(repository.findAll())
                .thenReturn(Flux.just(entity1, entity2));

        StepVerifier.create(adapter.findAll())
                .expectNextMatches(c -> c.getName().equals("Alice"))
                .expectNextMatches(c -> c.getName().equals("Bob"))
                .verifyComplete();
    }

    @Test
    void testUpdateCustomer() {
        var customer = Customer.builder()
                .name("UpdatedName")
                .docNumber("12345678")
                .build();

        var updatedEntity = CustomerEntity.builder()
                .name("UpdatedName")
                .docNumber("12345678")
                .build();

        Mockito.when(repository.save(Mockito.any(CustomerEntity.class)))
                .thenReturn(Mono.just(updatedEntity));

        Mono<Customer> result = adapter.update(customer);

        StepVerifier.create(result)
                .assertNext(updated -> Assertions.assertEquals("UpdatedName", updated.getName()))
                .verifyComplete();
    }

    @Test
    void testExistsByDocNumber() {
        Mockito.when(repository.existsByDocNumber("12345678"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByDocNumber("12345678"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void testDeleteCustomer() {
        String customerId = "123";

        // Simula que el delete fue exitoso (Mono.empty()).
        Mockito.when(repository.deleteById(customerId))
            .thenReturn(Mono.empty());

        Mono<Customer> result = adapter.delete(customerId);

        StepVerifier.create(result)
            .assertNext(deleted -> {
                Assertions.assertEquals(customerId, deleted.getId());
                Assertions.assertNull(deleted.getName()); // Ya que solo se retorna el ID
            })
            .verifyComplete();
    }




}
