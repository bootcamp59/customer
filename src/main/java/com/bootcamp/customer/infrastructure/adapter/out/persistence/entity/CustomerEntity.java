package com.bootcamp.customer.infrastructure.adapter.out.persistence.entity;

import com.bootcamp.customer.domain.model.Customer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(value = "customer")
@Getter
@Setter
@Builder
public class CustomerEntity {
    @Id
    private String id;

    private String name;
    private Customer.CustomerType type;

    @Field("doc_type")
    private String docType;

    @Field("doc_number")
    private String docNumber;

    private String email;
    private String phone;
    private String address;
    private Customer.PerfilType perfil;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("status")
    private String status;

    @Field("account_ids")
    private Set<String> accountIds;

    public enum CustomerType {
        PERSONAL, BUSINESS
    }

    public enum PerfilType {
        NORMAL, VIP
    }
}
