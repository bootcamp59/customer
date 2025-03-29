package com.bootcamp.customer.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
@Getter
@Setter
@Builder
public class Customer {

    @Id
    private String id;

    private String name;
    private CustomerType type;

    @Field("doc_type")
    private String docType;

    @Field("doc_number")
    private String docNumber;

    private String email;
    private String phone;


    @Field("account_ids")
    private Set<String> accountIds = new HashSet<>();

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("status")
    private byte status;

    public enum CustomerType {
        PERSONAL, BUSINESS
    }


}
