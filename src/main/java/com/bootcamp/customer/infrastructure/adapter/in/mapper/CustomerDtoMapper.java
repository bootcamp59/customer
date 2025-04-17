package com.bootcamp.customer.infrastructure.adapter.in.mapper;

import com.bootcamp.customer.domain.model.Customer;
import com.bootcamp.customer.rest.model.CustomerCreate;
import com.bootcamp.customer.rest.model.CustomerDto;

import java.time.ZoneOffset;

public class CustomerDtoMapper {

    public static Customer toModel(CustomerCreate create){
        return Customer.builder()
            .name(create.getName())
            .docType(create.getDocType().name())
            .docNumber(create.getDocNumber())
            .email(create.getEmail())
            .phone(create.getPhone())
            .address(create.getAddress())
            .type(Customer.CustomerType.valueOf(create.getType().name()))
            .perfil(Customer.PerfilType.valueOf(create.getPerfil().name()))
            .status(create.getStatus().name())
            .createdAt(create.getCreatedAt().toLocalDateTime())
            .updatedAt(create.getUpdatedAt().toLocalDateTime())
            .build();
    }

    public static CustomerDto toDto(Customer model){
        var dto = new CustomerDto();
        dto.setName(model.getName());
        dto.setDocType(CustomerDto.DocTypeEnum.fromValue(model.getDocType()));
        dto.setDocNumber(model.getDocNumber());
        dto.setEmail(model.getEmail());
        dto.setPhone(model.getPhone());
        dto.setAddress(model.getAddress());
        dto.setType(CustomerDto.TypeEnum.fromValue(model.getType().name()));
        dto.setPerfil(CustomerDto.PerfilEnum.valueOf(model.getPerfil().name()));
        dto.setStatus(CustomerDto.StatusEnum.fromValue(model.getStatus()));
        dto.setCreatedAt(model.getCreatedAt().atOffset(ZoneOffset.of("-05:00")));
        dto.setUpdatedAt(model.getUpdatedAt().atOffset(ZoneOffset.of("-05:00")));
        return dto;
    }
}
