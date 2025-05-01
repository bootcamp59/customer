package com.bootcamp.customer.infrastructure.adapter.out.client;

import com.bootcamp.customer.domain.dto.AccountDto;
import com.bootcamp.customer.infrastructure.config.CustomerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceClientAdapterTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private CustomerProperties properties;

    private AccountServiceClientAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new AccountServiceClientAdapter(webClient, properties);
    }

    @Test
    void testGetAccountByDocument_success() {
        List<AccountDto> expectedAccounts = List.of(
                AccountDto.builder().productoId("002").document("12345678").build(),
                AccountDto.builder().productoId("003").document("12345679").build()
        );

        when(properties.getMsAccountApi()).thenReturn("http://mock-api");
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://mock-api/123")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(AccountDto.class)).thenReturn(Flux.fromIterable(expectedAccounts));

        StepVerifier.create(adapter.getAccountByDocument("123"))
                .expectNext(expectedAccounts)
                .verifyComplete();
    }


}
