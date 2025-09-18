package com.furryhub.petservices.controller;

import com.furryhub.petservices.exception.ResourceNotFoundException;
import com.furryhub.petservices.model.dto.BookingDTO;
import com.furryhub.petservices.service.BookingService;
import com.furryhub.petservices.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController{
	
	  @Autowired
	  private BookingService bookingService;
       
	  @Autowired
	    private CustomerService customerService;
	  
	  @PostMapping("/create")
	  public ResponseEntity<BookingDTO> createBooking(
	          @RequestParam String customerEmail, @RequestParam Long packageId) 
	  {     
	          BookingDTO bookingDTO = bookingService.createBooking(customerEmail, packageId);
	          return ResponseEntity.ok(bookingDTO);	 
	  }
	  
	  @GetMapping("/by-customer-email")
	    public ResponseEntity<List<BookingDTO>> getBookingsByCustomerEmail(
	            @RequestParam String customerEmail
	    ) {
	        	List<BookingDTO> bookings = bookingService.getBookingsByCustomerEmail(customerEmail);
	            return ResponseEntity.ok(bookings);
	     
	    }
	  
	  @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
	        try {
	            customerService.deleteCustomer(id);
	            return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
	        } catch (ResourceNotFoundException e) {
	            return ResponseEntity.notFound().build(); // 404 Not Found if customer does not exist
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error for unexpected errors
	        }
	    }
}