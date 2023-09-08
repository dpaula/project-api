package com.lima.projectapi.client.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Fernando de Lima on 08/09/23
 */
@FeignClient(name = "customer", url = "${client.customer.url}")
public interface ICustomerClient {

    @GetMapping("/v1/clientes/{id}")
    CustomerResponse getCustomer(@PathVariable Integer id);
}
