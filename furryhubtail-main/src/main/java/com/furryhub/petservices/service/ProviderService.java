package com.furryhub.petservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furryhub.petservices.exception.ResourceNotFoundException;
import com.furryhub.petservices.model.entity.Provider;
import com.furryhub.petservices.model.entity.User;
import com.furryhub.petservices.repository.ProviderRepository;
import com.furryhub.petservices.repository.UserRepository;

@Service
public class ProviderService {
	
	@Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private UserRepository userRepository;

    public void deleteCustomer(Long id) {
        // Fetch the customer from the database
        Provider customer = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        // Delete associated user if required
        User user = customer.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        // Delete the customer
        providerRepository.delete(customer);
    }

}
