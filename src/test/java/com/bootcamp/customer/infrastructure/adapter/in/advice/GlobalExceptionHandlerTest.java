package com.bootcamp.customer.infrastructure.adapter.in.advice;

import com.bootcamp.customer.infrastructure.adapter.in.expose.CustomerController;
import com.bootcamp.customer.infrastructure.adapter.in.expose.DummyController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = DummyController.class)
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldHandleIllegalArgumentException() {
        webTestClient.get()
                .uri("/test-error")
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody()
                .jsonPath("$.status").isEqualTo(500)
                .jsonPath("$.error").isEqualTo("Internal Server Error")
                .jsonPath("$.message").isEqualTo("Dato inválido")
                .jsonPath("$.path").isEqualTo("/test-error")
                .jsonPath("$.timestamp").exists();  // Verifica que `timestamp` esté presente
    }
}
