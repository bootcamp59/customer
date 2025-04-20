package com.bootcamp.customer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsolidateProductoSummary {

    private String document;
    private String name;
    private List<AccountDto> accounts;
    private List<CreditDto> credits;
}
