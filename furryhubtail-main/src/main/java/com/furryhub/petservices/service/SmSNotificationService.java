package com.furryhub.petservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furryhub.petservices.model.entity.Customer;


@Service
public class SmSNotificationService {
	
	@Autowired
	TwilioService twilioService;
	
	public void bookingConfirmationaNotification(Customer customer,Long bookingId,String providerPhoneNumber)
	{
		//String customerPhoneNumber = customer.getPhoneNumber();
		String customerPhoneNumber = "+918999502997";
		 System.out.println(customerPhoneNumber);
         String customerName = customer.getUser().getFirstName() + " " + customer.getUser().getLastName();
         String message = String.format(
            "\n"+ "Dear %s, your booking with ID %d has been confirmed. By FurryHub Provider Contact Number %s "+"\n"+ "Thank you for choosing FurryHub!",
             customerName, bookingId,providerPhoneNumber
         );
         twilioService.sendSms(customerPhoneNumber, message);
	}

}
