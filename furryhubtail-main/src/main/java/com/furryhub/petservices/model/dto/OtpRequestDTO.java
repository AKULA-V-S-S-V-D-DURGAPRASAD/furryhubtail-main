package com.furryhub.petservices.model.dto;

public class OtpRequestDTO {
    private String email;
    private String purpose; // REGISTER, RESET, VERIFY
    private String code;    // for verification

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
