package com.bootcamp.customer.application.service;

import com.bootcamp.customer.application.port.out.AccountServiceClientPort;
import com.bootcamp.customer.application.port.out.CreditServiceClientPort;
import com.bootcamp.customer.application.port.out.CustomerRepositoryPort;
import com.bootcamp.customer.domain.dto.AccountDto;
import com.bootcamp.customer.domain.dto.CreditDto;
import com.bootcamp.customer.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

public class CustomerServiceTest {

    private CustomerRepositoryPort customerRepositoryPort;
    private AccountServiceClientPort accountServiceClientPort;
    private CreditServiceClientPort creditServiceClientPort;
    private CustomerService service;

    @BeforeEach
    void setUp() {
        customerRepositoryPort = Mockito.mock(CustomerRepositoryPort.class);
        accountServiceClientPort = Mockito.mock(AccountServiceClientPort.class);
        creditServiceClientPort = Mockito.mock(CreditServiceClientPort.class);
        service = new CustomerService(customerRepositoryPort, accountServiceClientPort, creditServiceClientPort);
    }

    @Test

    void testFindByDocNumber_found() {
        Customer customer = Customer.builder()
            .docNumber("12345678")
            .name("Roberth")
            .build();

        Mockito.when(customerRepositoryPort.findByDocNumber("12345678"))
            .thenReturn(Mono.just(customer));

        StepVerifier.create(service.findByDocNumber("12345678"))
            .expectNextMatches(c -> c.getName().equals("Roberth"))
            .verifyComplete();
    }

    @Test
    void testFindByDocNumber_notFound() {
        Mockito.when(customerRepositoryPort.findByDocNumber("0000"))
            .thenReturn(Mono.empty());

        StepVerifier.create(service.findByDocNumber("0000"))
            .expectErrorMatches(error -> error instanceof RuntimeException &&
                    error.getMessage().equals("Customer not found"))
            .verify();
    }

    @Test
    void testCreateCustomer_success() {
        Customer input = Customer.builder()
            .docNumber("123")
            .type(Customer.CustomerType.PERSONAL)
            .build();

        Mockito.when(customerRepositoryPort.existsByDocNumber("123"))
            .thenReturn(Mono.just(false));
        Mockito.when(customerRepositoryPort.create(any()))
            .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(service.create(input))
            .expectNextMatches(c -> c.getDocNumber().equals("123"))
            .verifyComplete();
    }

    @Test
    void testCreateCustomer_duplicateDocument() {
        Customer input = Customer.builder()
            .docNumber("123")
            .type(Customer.CustomerType.PERSONAL)
            .build();

        Mockito.when(customerRepositoryPort.existsByDocNumber("123"))
            .thenReturn(Mono.just(true));

        StepVerifier.create(service.create(input))
            .expectErrorMessage("Document number already exists")
            .verify();
    }

    @Test
    void testUpdateCustomer_success() {
        Customer existing = Customer.builder()
            .docNumber("123")
            .name("Old")
            .build();

        Customer updates = Customer.builder()
            .name("New")
            .build();

        Mockito.when(customerRepositoryPort.findByDocNumber("123"))
            .thenReturn(Mono.just(existing));
        Mockito.when(customerRepositoryPort.update(any()))
            .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(service.update("123", updates))
            .expectNextMatches(c -> c.getName().equals("New"))
            .verifyComplete();
    }

    @Test
    void testDeleteCustomer_withAccounts_fails() {
        Customer customer = Customer.builder()
            .docNumber("123")
            .accountIds(Set.of("acc1"))
            .build();

        Mockito.when(customerRepositoryPort.findByDocNumber("123"))
            .thenReturn(Mono.just(customer));

        StepVerifier.create(service.delete("123"))
            .expectErrorMessage("Cannot delete customer with active accounts")
            .verify();
    }

    @Test
    void testProductConsolidatedSummary() {
        Customer customer = Customer.builder()
            .docNumber("123")
            .name("Roberth")
            .build();

        var accounts = Collections.singletonList(AccountDto.builder().id("1").build());
        var credits = Collections.singletonList(CreditDto.builder().balance(200.00).build());

        Mockito.when(accountServiceClientPort.getAccountByDocument("123"))
            .thenReturn(Mono.just(accounts));
        Mockito.when(creditServiceClientPort.getCreditByDocument("123"))
            .thenReturn(Mono.just(credits));
        Mockito.when(customerRepositoryPort.findByDocNumber("123"))
            .thenReturn(Mono.just(customer));

        StepVerifier.create(service.productConsolidatedSummary("123"))
            .expectNextMatches(summary ->
                summary.getDocument().equals("123") &&
                    summary.getName().equals("Roberth") &&
                    summary.getAccounts().equals(accounts) &&
                    summary.getCredits().equals(credits)
            )
            .verifyComplete();
    }
}
