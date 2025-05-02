package com.bootcamp.customer.infrastructure.adapter.in.expose;

import com.bootcamp.customer.application.port.in.CustomerUseCase;
import com.bootcamp.customer.domain.dto.ConsolidateProductoSummary;
import com.bootcamp.customer.domain.model.Customer;
import com.bootcamp.customer.infrastructure.adapter.in.mapper.CustomerDtoMapper;
import com.bootcamp.customer.rest.model.CustomerCreate;
import com.bootcamp.customer.rest.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class CustomerControllerTest {

    private WebTestClient webTestClient;
    private CustomerUseCase usecaseMock;
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        // Crear un mock del servicio CustomerUseCase
        usecaseMock = Mockito.mock(CustomerUseCase.class);

        // Crear el controlador con el mock
        customerController = new CustomerController(usecaseMock);

        // Configurar WebTestClient para usar el controlador
        webTestClient = WebTestClient.bindToController(customerController).build();
    }



    @Test
    void testProductConsolidatedSummary() {
        ConsolidateProductoSummary summary = new ConsolidateProductoSummary();

        // Simulación del comportamiento del servicio para obtener un resumen consolidado de productos
        when(usecaseMock.productConsolidatedSummary("123456")).thenReturn(Mono.just(summary));

        // Verificar la respuesta de la API cuando se hace un GET para obtener el resumen consolidado de productos
        webTestClient.get()
                .uri("/api/v1/customer/{document}/consolidate-summary", "123456")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConsolidateProductoSummary.class)
                .isEqualTo(summary);  // Verifica que el resumen consolidado devuelto sea el esperado
    }
}
