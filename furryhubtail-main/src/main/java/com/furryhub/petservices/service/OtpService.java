package com.furryhub.petservices.service;

import com.furryhub.petservices.model.entity.Customer;
import com.furryhub.petservices.model.entity.Provider;
import com.furryhub.petservices.model.entity.User;
import com.furryhub.petservices.util.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    @Autowired
    private EmailService emailService; // util.EmailService in your project

    /**
     * Sms service is optional — if you add a richer implementation later it will be used.
     * We mark it as not required in case you don't have an SMS provider configured.
     */
    @Autowired(required = false)
    private SmSNotificationService smsNotificationService;

    // simple in-memory cache { email -> otp }. Replace with DB/cache in production.
    private final ConcurrentMap<String, String> otpCache = new ConcurrentHashMap<>();

    /**
     * Generate a 6-digit OTP and send it to user's email (and SMS if phone available).
     */
    public void generateAndSendOtp(User user) {
        if (user == null || user.getEmail() == null) {
            logger.warn("generateAndSendOtp called with null user or email");
            return;
        }

        String otp = String.format("%06d", new Random().nextInt(900_000) + 0);
        otpCache.put(user.getEmail(), otp);

        // Send email using the util EmailService (sendEmail method)
        String to = user.getEmail();
        String subject = "Your FurryHub OTP";
        String text = "Dear " + safeFirstName(user) + ",\n\n"
                + "Your OTP is: " + otp + "\n\n"
                + "This OTP expires in 10 minutes.\n\n"
                + "Thank you,\nFurryHub Team";

        try {
            emailService.sendEmail(to, subject, text);
            logger.info("OTP email sent to {}", to);
        } catch (Exception ex) {
            logger.error("Failed to send OTP email to {}: {}", to, ex.getMessage(), ex);
        }

        // Attempt SMS if service is present and we can find a phone number
        if (smsNotificationService != null) {
            String phone = findPhoneNumber(user);
            if (phone != null && !phone.isBlank()) {
                try {
                    smsNotificationService.sendSms(phone, "Your FurryHub OTP is: " + otp);
                    logger.info("OTP SMS sent to {}", phone);
                } catch (Exception ex) {
                    logger.error("Failed to send OTP SMS to {}: {}", phone, ex.getMessage(), ex);
                }
            }
        }
    }

    /**
     * Validate the OTP previously generated for this email.
     * Returns true if match; consumes the OTP (removes from cache) on success.
     */
    public boolean validateOtp(String email, String inputOtp) {
        if (email == null || inputOtp == null) return false;
        String cached = otpCache.get(email);
        boolean ok = cached != null && cached.equals(inputOtp);
        if (ok) otpCache.remove(email);
        return ok;
    }

    /**
     * Helper to find a phone number from User -> Customer/Provider or user.phone field.
     */
    private String findPhoneNumber(User user) {
        if (user == null) return null;
        // prefer phone on User if populated
        try {
            if (user.getPhone() != null && !user.getPhone().isBlank()) return user.getPhone();
        } catch (Throwable ignored) {}

        // try customer
        Customer c = user.getCustomer();
        if (c != null) {
            try {
                if (c.getPhoneNumber() != null && !c.getPhoneNumber().isBlank()) return c.getPhoneNumber();
            } catch (Throwable ignored) {}
        }

        // try provider
        Provider p = user.getProvider();
        if (p != null) {
            try {
                if (p.getPhoneNumber() != null && !p.getPhoneNumber().isBlank()) return p.getPhoneNumber();
            } catch (Throwable ignored) {}
        }

        return null;
    }

    private String safeFirstName(User user) {
        if (user == null) return "";
        try {
            return user.getFirstName() == null ? "" : user.getFirstName();
        } catch (Throwable t) {
            return "";
        }
    }
}
