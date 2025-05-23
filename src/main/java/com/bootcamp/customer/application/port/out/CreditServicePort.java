package com.bootcamp.customer.application.port.out;

import com.bootcamp.customer.domain.dto.CreditDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CreditServicePort {

    Mono<List<CreditDto>> getCreditByDocument(String documentNumber);
}
