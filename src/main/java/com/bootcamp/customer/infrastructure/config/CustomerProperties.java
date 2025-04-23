package com.bootcamp.customer.infrastructure.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "customer")
@Getter
@Setter
public class CustomerProperties {

    private String msAccountApi;
    private String msCreditApi;
}
