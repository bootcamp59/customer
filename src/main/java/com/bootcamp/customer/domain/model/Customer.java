package com.bootcamp.customer.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class Customer {

    private String name;
    private CustomerType type;

    private String docType;
    private String docNumber;

    private String email;
    private String phone;
    private String address;
    private PerfilType perfil;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    private Set<String> accountIds;

    public enum CustomerType {
        PERSONAL, EMPRESARIAL
    }

    public enum PerfilType {
        NORMAL, VIP, PYME
    }

}
