
package com.furryhub.petservices.service;

import com.furryhub.petservices.model.dto.BookingDTO;
import com.furryhub.petservices.model.entity.Booking;
import com.furryhub.petservices.model.entity.Customer;
import com.furryhub.petservices.model.entity.Package;
import com.furryhub.petservices.model.entity.Provider;
import com.furryhub.petservices.model.entity.BookingStatus;
import com.furryhub.petservices.repository.BookingRepository;
import com.furryhub.petservices.repository.CustomerRepository;
import com.furryhub.petservices.repository.PackageRepository;
import com.furryhub.petservices.repository.ProviderRepository;
import com.furryhub.petservices.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private BookingRepository bookingRepository;
    
    @Transactional
    public BookingDTO createBooking(String customerEmail, Long packageId) {
        Booking booking = new Booking();

        
        Customer customer = customerRepository.findByUser_Email(customerEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Package aPackage = packageRepository.findById(packageId)
            .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        booking.setCustomer(customer);
        booking.setAPackage(aPackage);
        booking.setProvider(aPackage.getProvider());
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(aPackage.getPrice());

        booking = bookingRepository.save(booking);

        return convertToDTO(booking); 
    }

  
    @Transactional
    public BookingDTO confirmBooking(Long bookingId, String providerEmail) {
        
        Provider provider = providerRepository.findByUser_Email(providerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with email: " + providerEmail));

       
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        // Ensure the provider is the one associated with the booking
        if (!booking.getProvider().getId().equals(provider.getId())) {
            throw new IllegalStateException("Provider is not authorized to confirm this booking.");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());

        // Save the updated Booking
        Booking updatedBooking = bookingRepository.save(booking);

        // Convert to DTO
        return convertToDTO(updatedBooking);
    }
    
    private BookingDTO convertToDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .customerId(booking.getCustomer().getId())
                .packageId(booking.getAPackage().getId())
                .providerId(booking.getProvider().getId())
                .providerPhoneNumber(booking.getProvider().getPhoneNumber())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }


 
    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsByCustomerEmail(String email) {
    	List<BookingDTO> bookings = bookingRepository.findBookingsByCustomerEmail(email);
    	if (bookings.isEmpty()) {
    	    throw new ResourceNotFoundException("No bookings found for the customer");
    	}
    	return bookings;
    }
}