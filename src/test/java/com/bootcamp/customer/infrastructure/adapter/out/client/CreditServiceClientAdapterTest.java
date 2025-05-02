package com.bootcamp.customer.infrastructure.adapter.out.client;

import com.bootcamp.customer.infrastructure.config.CustomerProperties;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

public class CreditServiceClientAdapterTest {

    private MockWebServer mockWebServer;
    private CreditServiceClientAdapter adapter;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();

        CustomerProperties properties = new CustomerProperties();
        properties.setMsCreditApi(baseUrl);

        WebClient webClient = WebClient.create();

        adapter = new CreditServiceClientAdapter(webClient, properties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getCreditByDocument_shouldReturnList() {
        String jsonResponse = """
                [
                  {"creditId": "1", "amount": 1000},
                  {"creditId": "2", "amount": 2000}
                ]
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(adapter.getCreditByDocument("12345678"))
                .expectNextMatches(list -> list.size() == 2)
                .verifyComplete();
    }
}
