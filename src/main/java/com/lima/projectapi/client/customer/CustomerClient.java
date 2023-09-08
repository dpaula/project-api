package com.lima.projectapi.client.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Fernando de Lima on 08/09/23
 */
@Slf4j
@Component
@AllArgsConstructor
public class CustomerClient {

    private final ICustomerClient client;

    public CustomerResponse getCustomer(final Integer customerId) {
        log.info("Buscando cliente id: {}", customerId);
        return client.getCustomer(customerId);
    }
}
