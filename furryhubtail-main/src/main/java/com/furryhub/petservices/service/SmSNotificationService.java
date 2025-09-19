package com.furryhub.petservices.service;

import com.furryhub.petservices.model.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmSNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(SmSNotificationService.class);

	/**
	 * Send a generic SMS
	 */
	public void sendSms(String phoneNumber, String message) {
		// Implement actual SMS API call here (Twilio, AWS SNS, etc.)
		logger.info("Simulated SMS to {}: {}", phoneNumber, message);
	}

	/**
	 * Booking confirmation notification SMS
	 */
	public void bookingConfirmationaNotification(Customer customer, Long bookingId, String extraText) {
		if (customer == null) {
			logger.warn("Customer is null, cannot send SMS");
			return;
		}

		// names from associated user
		String firstName = customer.getUser() != null && customer.getUser().getFirstName() != null
				? customer.getUser().getFirstName() : "";
		String lastName = customer.getUser() != null && customer.getUser().getLastName() != null
				? customer.getUser().getLastName() : "";

		// phone from customer entity
		String phone = customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "";

		if (phone.isEmpty()) {
			logger.warn("Customer {} {} has no phone number", firstName, lastName);
			return;
		}

		String msg = "Dear " + (firstName + " " + lastName).trim()
				+ ", your booking #" + bookingId + " is confirmed. " + extraText;

		sendSms(phone, msg);
	}
}
