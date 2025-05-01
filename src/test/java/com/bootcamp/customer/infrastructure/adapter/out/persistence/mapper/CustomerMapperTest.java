package com.bootcamp.customer.infrastructure.adapter.out.persistence.mapper;

import com.bootcamp.customer.domain.model.Customer;
import com.bootcamp.customer.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {

    @Test
    void testToModel() {
        // Arrange
        CustomerEntity entity = CustomerEntity.builder()
            .id("1")
            .name("John Doe")
            .docType("DNI")
            .docNumber("12345678")
            .email("johndoe@example.com")
            .phone("123456789")
            .address("123 Main St")
            .type(Customer.CustomerType.PERSONAL)
            .perfil(Customer.PerfilType.NORMAL)
            .status("Active")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        // Act
        Customer customer = CustomerMapper.toModel(entity);

        // Assert
        assertNotNull(customer);
        assertEquals(entity.getId(), customer.getId());
        assertEquals(entity.getName(), customer.getName());
        assertEquals(entity.getDocType(), customer.getDocType());
        assertEquals(entity.getDocNumber(), customer.getDocNumber());
        assertEquals(entity.getEmail(), customer.getEmail());
        assertEquals(entity.getPhone(), customer.getPhone());
        assertEquals(entity.getAddress(), customer.getAddress());
        assertEquals(entity.getType(), customer.getType());
        assertEquals(entity.getPerfil(), customer.getPerfil());
        assertEquals(entity.getStatus(), customer.getStatus());
        assertEquals(entity.getCreatedAt(), customer.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), customer.getUpdatedAt());
    }

    @Test
    void testToEntity() {
        // Arrange
        Customer customer = Customer.builder()
            .id("1")
            .name("John Doe")
            .docType("DNI")
            .docNumber("12345678")
            .email("johndoe@example.com")
            .phone("123456789")
            .address("123 Main St")
            .type(Customer.CustomerType.PERSONAL)
            .perfil(Customer.PerfilType.NORMAL)
            .status("Active")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        // Act
        CustomerEntity entity = CustomerMapper.toEntity(customer);

        // Assert
        assertNotNull(entity);
        assertEquals(customer.getId(), entity.getId());
        assertEquals(customer.getName(), entity.getName());
        assertEquals(customer.getDocType(), entity.getDocType());
        assertEquals(customer.getDocNumber(), entity.getDocNumber());
        assertEquals(customer.getEmail(), entity.getEmail());
        assertEquals(customer.getPhone(), entity.getPhone());
        assertEquals(customer.getAddress(), entity.getAddress());
        assertEquals(customer.getType(), entity.getType());
        assertEquals(customer.getPerfil(), entity.getPerfil());
        assertEquals(customer.getStatus(), entity.getStatus());
        assertEquals(customer.getCreatedAt(), entity.getCreatedAt());
        assertEquals(customer.getUpdatedAt(), entity.getUpdatedAt());
    }

}
