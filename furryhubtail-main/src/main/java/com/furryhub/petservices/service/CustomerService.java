package com.furryhub.petservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furryhub.petservices.exception.ResourceNotFoundException;
import com.furryhub.petservices.model.entity.Customer;
import com.furryhub.petservices.model.entity.User;
import com.furryhub.petservices.repository.CustomerRepository;
import com.furryhub.petservices.repository.UserRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public void deleteCustomer(Long id) {
        // Fetch the customer from the database
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // Delete associated user if required
        User user = customer.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        // Delete the customer
        customerRepository.delete(customer);
    }
}