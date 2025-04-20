package com.bootcamp.customer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreditDto {


    private String id;
    private String document;
    private CreditType type;
    private String bankName;
    private String productoId;
    private Double linea; //linea de credito/prestamo
    private Double balance;
    private Double interestRate;
    private LocalDate openingDate;
    private LocalDate dueDate;

    // Common fields for all credit types
    private Integer paymentDay; // Day of month for payments
    private Integer cutOfDay;
    private Integer remainingInstallments; // cuotas restantes

    // Fields specific to credit cards
    private Double creditUsageToPay; // credito usado por pagar
    private LocalDate paymentDate; // For credit cards

    public enum CreditType {
        PERSONAL, EMPRESARIAL, CREDIT_CARD
    }
}
