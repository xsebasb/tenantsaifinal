package com.gruposai.tenantmanager.Service;

import com.gruposai.tenantmanager.DTO.CustomerProjection;
import com.gruposai.tenantmanager.Repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerProjection> findCustomerByIdN(String id_n) {
        return customerRepository.findByIdN(id_n);
    }
}
