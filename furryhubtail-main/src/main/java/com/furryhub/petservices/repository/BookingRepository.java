
package com.furryhub.petservices.repository;

import com.furryhub.petservices.model.dto.BookingDTO;
import com.furryhub.petservices.model.entity.Booking;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
	List<Booking> findByCustomer_Id(Long customerId);

	Optional<Booking> findById(Long bookingId);
	@Query("SELECT b FROM Booking b JOIN FETCH b.customer WHERE b.id = :bookingId")
	Optional<Booking> findBookingWithCustomer(@Param("bookingId") Long bookingId);
	
	@Query("SELECT new com.furryhub.petservices.model.dto.BookingDTO(" +
		       "b.id, " +
		       "b.customer.id, " +
		       "b.aPackage.id, " +
		       "b.provider.id, " +
		       "b.bookingDate, " +
		       "b.status, " +
		       "b.totalPrice, " +
		       "b.createdAt, " +
		       "b.updatedAt) " +
		       "FROM Booking b " +
		       "WHERE b.customer.user.email = :email")
		List<BookingDTO> findBookingsByCustomerEmail(@Param("email") String email);
    
}