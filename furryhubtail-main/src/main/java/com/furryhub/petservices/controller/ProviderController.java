package com.furryhub.petservices.controller;
import com.furryhub.petservices.exception.ResourceNotFoundException;
import com.furryhub.petservices.model.dto.BookingDTO;
import com.furryhub.petservices.model.dto.PackageRequestDTO;
import com.furryhub.petservices.model.entity.Booking;
import com.furryhub.petservices.model.entity.Customer;
import com.furryhub.petservices.model.entity.Package;
import com.furryhub.petservices.repository.BookingRepository;
import com.furryhub.petservices.service.BookingService;
import com.furryhub.petservices.service.PackageService;
import com.furryhub.petservices.service.ProviderService;
import com.furryhub.petservices.service.SmSNotificationService;
import com.furryhub.petservices.service.TwilioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/provider")
public class ProviderController{	   
	    @Autowired
	    private PackageService packageService;
	    @Autowired
		private BookingRepository bookingRepository;
		@Autowired
		 private BookingService bookingService;	
		@Autowired
		 private ProviderService providerService;
		
		@Autowired
		private SmSNotificationService smsServiceNotification;
		  
		  
	@PostMapping("/add")
    public ResponseEntity<PackageRequestDTO> addPackage(@Valid @RequestBody PackageRequestDTO packageRequestDTO) {
		PackageRequestDTO aPackage = packageService.addPackage(
	            packageRequestDTO.getProviderEmail(),
	            packageRequestDTO.getName(),
	            packageRequestDTO.getDescription(),
	            packageRequestDTO.getPrice(),
	            packageRequestDTO.getDuration()
	        );
	        return ResponseEntity.ok(aPackage);
	    }
	
	@GetMapping("/all")
    public ResponseEntity<Iterable<Package>> getAllPackages() {
        Iterable<Package> packages = packageService.getAllPackages();
        return ResponseEntity.ok(packages);
    }
	
	
	  @PutMapping("/{bookingId}/confirm")
	  public ResponseEntity<BookingDTO> confirmBooking(
	          @PathVariable Long bookingId,
	          @RequestParam String providerEmail
	  ) {
	      try {
	          BookingDTO bookingDTO = bookingService.confirmBooking(bookingId, providerEmail);
	          Booking booking = bookingRepository.findBookingWithCustomer(bookingId)
	        	        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
	         
	          
	          Customer customer = booking.getCustomer();
	          String providerPhoneNumber = bookingDTO.getProviderPhoneNumber();
	          smsServiceNotification.bookingConfirmationaNotification(customer,bookingId,providerPhoneNumber);
	          return ResponseEntity.ok(bookingDTO);
	      } catch (ResourceNotFoundException e) {
	          return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                  .body(null); // Handle appropriately
	      } catch (IllegalStateException e) {
	          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                  .body(null); // Handle appropriately
	      }
	  }
	  
	  
	  @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
	        try {
	        	providerService.deleteCustomer(id);
	            return ResponseEntity.noContent().build(); // 204 No Content on successful deletion
	        } catch (ResourceNotFoundException e) {
	            return ResponseEntity.notFound().build(); // 404 Not Found if customer does not exist
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error for unexpected errors
	        }
	    }

	
}