package com.bootcamp.customer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountDto {

    private String id;
    private String productoId;
    private String document;
    private AccountType type;
    private double comisionMantenimiento;
    private double limiteMovimientosMensual;
    private double diaMovimientoPermitido;
    private LocalDateTime fechaApertura;
    private String banco;
    private Set<String> titulares;
    private Set<String> firmantes;
    private double saldo;
    private double montoNimimoPromedioMensual;
    private double transaccionesSinComision;
    private double comisionPorTransaccionExcedente;
    private LocalDateTime fechaUltimaTransacion;

    public enum AccountType {
        AHORRO(),

        CUENTA_CORRIENTE(),

        PLAZO_FIJO();

    }
}
