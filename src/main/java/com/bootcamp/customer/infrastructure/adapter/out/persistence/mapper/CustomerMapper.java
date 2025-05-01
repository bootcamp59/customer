package com.bootcamp.customer.infrastructure.adapter.out.persistence.mapper;

import com.bootcamp.customer.domain.model.Customer;
import com.bootcamp.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;

public class CustomerMapper {

    public static Customer toModel(CustomerEntity entity){
        return Customer.builder()
                .id(entity.getId())
                .name(entity.getName())
                .docType(entity.getDocType())
                .docNumber(entity.getDocNumber())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .type(entity.getType())
                .perfil(entity.getPerfil())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static CustomerEntity toEntity(Customer model){
        return CustomerEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .docType(model.getDocType())
                .docNumber(model.getDocNumber())
                .email(model.getEmail())
                .phone(model.getPhone())
                .address(model.getAddress())
                .type(model.getType())
                .perfil(model.getPerfil())
                .status(model.getStatus())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }
}
