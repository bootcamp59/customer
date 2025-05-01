package com.bootcamp.customer.application.port.out;

import com.bootcamp.customer.domain.dto.AccountDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AccountServiceClientPort {

    Mono<List<AccountDto>> getAccountByDocument(String documentNumber);
}
