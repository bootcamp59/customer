package com.bootcamp.customer.infrastructure.adapter.in.expose;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @GetMapping("/test-error")
    public String throwError() {
        throw new IllegalArgumentException("Dato inválido");
    }
}
