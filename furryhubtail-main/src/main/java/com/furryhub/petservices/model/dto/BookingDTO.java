
package com.furryhub.petservices.model.dto;

import com.furryhub.petservices.model.entity.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private Long id;
    private Long customerId;
    private Long packageId;
    private Long providerId;
    private String providerPhoneNumber;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}