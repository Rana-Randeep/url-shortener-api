package com.urlshortener.dto.response;

public class AuthResponse {

    private String token;
    private String email;

    public AuthResponse() {
    }

    public AuthResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }

    public void setToken(String token) { this.token = token; }
    public void setEmail(String email) { this.email = email; }
}